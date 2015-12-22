package com.kh.tong.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.kh.json.GsonHelper;
import com.kh.tong.web.bean.TrackProgress;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.db.DBManager;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;
import com.ximalaya.sdk4j.Albums;
import com.ximalaya.sdk4j.Banners;
import com.ximalaya.sdk4j.Categories;
import com.ximalaya.sdk4j.Ranks;
import com.ximalaya.sdk4j.Searches;
import com.ximalaya.sdk4j.Tracks;
import com.ximalaya.sdk4j.model.Paging;
import com.ximalaya.sdk4j.model.XimalayaException;
import com.ximalaya.sdk4j.model.dto.album.Album;
import com.ximalaya.sdk4j.model.dto.album.AlbumList;
import com.ximalaya.sdk4j.model.dto.album.AlbumTracks;
import com.ximalaya.sdk4j.model.dto.banner.Banner;
import com.ximalaya.sdk4j.model.dto.category.Category;
import com.ximalaya.sdk4j.model.dto.rank.Rank;
import com.ximalaya.sdk4j.model.dto.search.HotWord;
import com.ximalaya.sdk4j.model.dto.track.Track;
import com.ximalaya.sdk4j.model.dto.track.TrackList;

public class AlbumServlet extends HttpServlet {

    private static final long serialVersionUID = 2929144134164563366L;
    static Logger log = Logger.getLogger(AlbumServlet.class);
    public static final String ParamAction = "act";
    public static final String ParamImageScale = "image_scale";
    public static final String ParamAlbumId = "album_id";
    public static final String ParamTrackId = "track_id";
    public static final String ParamCategoryId = "category_id";
    public static final String ParamPage = "page";
    public static final String ParamCount = "count";
    public static final String ParamSort = "sort";
    public static final String ParamRankKey = "rank_key";
    public static final String ParamTop = "top";
    public static final String ParamQuery = "q";
    public static final String ParamTrackProgress = "progress";
    public static final String ParamUserUUID = "user_uuid";

    public static final String ActionGetBannerList = "get_banner_list";
    public static final String ActionGetAlbumById = "get_album_by_id";
    public static final String ActionGetTrackById = "get_track_by_id";
    public static final String ActionGetCategoryList = "get_category_list";
    public static final String ActionGetCategoryAlbumList = "get_category_album_list";
    public static final String ActionGetAlbumTrackList = "get_album_track_list";
    public static final String ActionGetRankList = "get_rank_List";
    public static final String ActionGetRankTrackList = "get_rank_track_list";
    public static final String ActionGetRankAlbumList = "get_rank_album_list";
    public static final String ActionGetHotWordList = "get_hot_word_list";
    public static final String ActionSearchAlbum = "search_album";
    public static final String ActionGetUserAlbumList = "get_user_album_list";
    public static final String ActionUpdateUserAlbumIdList = "update_user_album_id_list";
    public static final String ActionAddUserAlbumIdList = "add_user_album_id_list";
    public static final String ActionDeleteUserAlbumIdList = "delete_user_album_id_list";
    public static final String ActionSavePlayStatus = "save_play_status";
    public static final String ActionGetPlayStatus = "get_play_status";
    public static final String ActionGetSpicifyTrackList = "get_spicify_track_list";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doWork(request, response);
        } catch (XimalayaException e) {
            log.error("XimalayaException:" + e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doWork(request, response);
        } catch (XimalayaException e) {
            log.error("XimalayaException:" + e);
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,
            XimalayaException {
        String callback = request.getParameter(WebConstants.RequestParamJsonpCallback);
        WebResponse result = new WebResponse();
        String action = request.getParameter(ParamAction);
        User userSession = null;
        if (request.getSession() != null) {
            userSession = (User) request.getSession().getAttribute(WebConstants.SessionAttributeUser);
        }
        String currentUserUUID = null;
        if (userSession != null) {
            currentUserUUID = userSession.getUuid();
        }
        if (ActionGetBannerList.equals(action)) {
            // String imageScale = request.getParameter(ParamImageScale);
            Banners banners = new Banners();
            // List<Banner> bannerList = banners.getDiscoveryBanners(channel,
            // appVersion, imageScale);
            List<Banner> bannerList = banners.getDiscoveryBanners();
            List<Banner> bannerList2 = new ArrayList<Banner>();
            for (int i = 0; i < bannerList.size(); i++) {
                String bannerContentType = bannerList.get(i).getBannerContentType();
                if ("2".equals(bannerContentType) || "3".equals(bannerContentType)) {
                    bannerList2.add(bannerList.get(i));
                }
            }
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(bannerList2);
        } else if (ActionGetAlbumById.equals(action)) {
            long albumId = WebUtil.parseLong(request.getParameter(ParamAlbumId), -1);
            if (albumId < 0) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("albumId参数错误！");
            } else {
                Albums albums = new Albums();
                long[] albumIdArray = new long[1];
                albumIdArray[0] = albumId;
                List<Album> albumlList = albums.batchGetAlbums(albumIdArray);
                if (albumlList == null || albumlList.size() == 0) {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    result.setData(null);
                } else {
                    // 是否在用户收藏列表中?改变isCollected属性为true
                    List<Long> collectedAlbumIDList = DBManager.getCollectAlbumIdList(currentUserUUID);
                    if (collectedAlbumIDList.contains(albumlList.get(0).getId())) {
                        albumlList.get(0).setCollected(true);
                    }
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    result.setData(albumlList.get(0));
                }
            }
        } else if (ActionGetTrackById.equals(action)) {
            long trackId = WebUtil.parseLong(request.getParameter(ParamTrackId), -1);
            if (trackId < 0) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("trackId参数错误！");
            } else {
                Tracks tracks = new Tracks();
                long[] trackIdArray = new long[1];
                trackIdArray[0] = trackId;
                List<Track> trackslList = tracks.batchGetTracks(trackIdArray);
                if (trackslList == null) {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    result.setData(null);
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    result.setData(trackslList.get(0));
                }
            }
        } else if (ActionGetCategoryList.equals(action)) {
            Categories categories = new Categories();
            List<Category> categorList = categories.getCategories();
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(categorList);
        } else if (ActionGetCategoryAlbumList.equals(action)) {
            long categoryID = WebUtil.parseLong(request.getParameter(ParamCategoryId), 0);
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            Paging paging = new Paging(page, count);
            Albums albums = new Albums();
            AlbumList albumList = albums.getAllCopyrightAlbumList(categoryID, null, paging);
            // 是否在用户收藏列表中?改变isCollected属性为true
            List<Long> collectedAlbumIDList = DBManager.getCollectAlbumIdList(currentUserUUID);
            List<Album> list = albumList.getAlbums();
            for (Album album : list) {
                if (collectedAlbumIDList.contains(album.getId())) {
                    album.setCollected(true);
                }
            }
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(albumList);
        } else if (ActionGetAlbumTrackList.equals(action)) {
            long albumId = WebUtil.parseLong(request.getParameter(ParamAlbumId), -1);
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            String sort = request.getParameter(ParamSort);
            Paging paging = new Paging(page, count);
            if (albumId < 0) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("albumId参数错误！");
            } else {
                if (!"asc".equals(sort) && !"desc".equals(sort)) {
                    sort = "asc";
                }
                Albums albums = new Albums();
                AlbumTracks albumTracks = albums.browseAlbumTracks(albumId, paging, sort);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(albumTracks);
            }
        } else if (ActionGetRankList.equals(action)) {
            Ranks ranks = new Ranks();
            List<Rank> ranksList = ranks.getFirstPageRanks(1);
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(ranksList);
        } else if (ActionGetRankTrackList.equals(action)) {
            String rankKey = request.getParameter(ParamRankKey);
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            Paging paging = new Paging(page, count);
            if (rankKey == null || rankKey.isEmpty()) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("rankKey参数不可为空！");
            } else {
                Ranks ranks = new Ranks();
                TrackList trackList = ranks.getRankTracks(rankKey, paging);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(trackList);
            }
        } else if (ActionGetRankAlbumList.equals(action)) {
            String rankKey = request.getParameter(ParamRankKey);
            String rankKey1 = "ranking:track:played:1:0";
            String rankKey2 = "ranking:album:subscribed:30:0";
            String rankKey3 = "ranking:album:custom:42";
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            Paging paging = new Paging(page, count);
            if (rankKey1.equals(rankKey)) {
                Ranks ranks = new Ranks();
                TrackList trackList = ranks.getRankTracks(rankKey, paging);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(trackList);
            } else if (rankKey2.equals(rankKey) || rankKey3.equals(rankKey)) {
                Ranks ranks = new Ranks();
                AlbumList albumList = ranks.getRankAlbums(rankKey, paging);
                // 是否在用户收藏列表中?改变isCollected属性为true
                List<Long> collectedAlbumIDList = DBManager.getCollectAlbumIdList(currentUserUUID);
                List<Album> list = albumList.getAlbums();
                for (Album album : list) {
                    if (collectedAlbumIDList.contains(album.getId())) {
                        album.setCollected(true);
                    }
                }
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(albumList);
            } else {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("rankKey参数错误！");
            }
        } else if (ActionGetHotWordList.equals(action)) {
            int top = WebUtil.parseInt(request.getParameter(ParamTop), 10);
            if (top < 1 || top > 20) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("1<=top<=20");
            } else {
                Searches searches = new Searches();
                List<HotWord> hotWords = searches.searchHotWords(top);
                List<String> searchWord = new ArrayList<String>();
                for (HotWord hotWord : hotWords) {
                    searchWord.add(hotWord.getSearchword());
                }
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(searchWord);
            }
        } else if (ActionSearchAlbum.equals(action)) {
            String q = request.getParameter(ParamQuery);
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            Paging paging = new Paging(page, count);
            if (q == null || q.isEmpty()) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("q参数不可为空！");
            } else {
                Searches searches = new Searches();
                AlbumList albumList = searches.searchAlbumList(q, 0, paging);
                // 是否在用户收藏列表中?改变isCollected属性为true
                List<Long> collectedAlbumIDList = DBManager.getCollectAlbumIdList(currentUserUUID);
                List<Album> list = albumList.getAlbums();
                for (Album album : list) {
                    if (collectedAlbumIDList.contains(album.getId())) {
                        album.setCollected(true);
                    }
                }
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(albumList);
            }
        } else if (ActionGetUserAlbumList.equals(action)) {
            // 优先取user_uuid参数中的用户Uuid
            String userUUID = request.getParameter(ParamUserUUID);
            String dbUUID = "";
            if (userUUID != null) {
                dbUUID = userUUID;
            } else {
                dbUUID = currentUserUUID;
            }
            List<Long> albumIDList = DBManager.getCollectAlbumIdList(dbUUID);
            Albums albums = new Albums();
            long[] albumIdArray = new long[albumIDList.size()];
            for (int i = 0; i < albumIDList.size(); i++) {
                albumIdArray[i] = albumIDList.get(i);
            }
            List<Album> albumList = albums.batchGetAlbums(albumIdArray);
            // isCollected 为true
            for (Album album : albumList) {
                album.setCollected(true);
            }
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(albumList);
        } else if (ActionUpdateUserAlbumIdList.equals(action)) {
            String data = request.getParameter(ParamAlbumId);
            List<Long> albumIdList = Util.parseListLong(data);
            boolean ret = DBManager.updateCollectAlbumList(albumIdList, currentUserUUID);
            if (!ret) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("服务器错误！");
            } else {
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            }
        } else if (ActionAddUserAlbumIdList.equals(action)) {
            String data = request.getParameter(ParamAlbumId);
            List<Long> albumIdList = Util.parseListLong(data);
            if (albumIdList == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("albumIdList错误");
            } else {
                boolean ret = DBManager.addCollectAlbumList(albumIdList, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }

        } else if (ActionDeleteUserAlbumIdList.equals(action)) {
            String data = request.getParameter(ParamAlbumId);
            List<Long> albumIdList = Util.parseListLong(data);
            if (albumIdList == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("albumIdList错误");
            } else {
                boolean ret = DBManager.deleteCollectAlbumList(albumIdList, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionSavePlayStatus.equals(action)) {
            String data = request.getParameter(ParamTrackProgress);
            TrackProgress progress = Util.parseTrackProgress(data);
            if (progress == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("progress错误");
            } else {
                boolean ret = DBManager.insertLastTrackProgress(progress, currentUserUUID);
                DBManager.updateCollectAlbumListTrackProgress(progress, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionGetPlayStatus.equals(action)) {
            TrackProgress trackProgress = DBManager.getLastTrackProgress(currentUserUUID);
            List<TrackProgress> trackProgressList = DBManager.getCollectTrackProgress(currentUserUUID);
            AbstractMap.SimpleImmutableEntry<List<TrackProgress>, TrackProgress> maEntry = new SimpleImmutableEntry<List<TrackProgress>, TrackProgress>(
                    trackProgressList, trackProgress);
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(maEntry);
        } else if (ActionGetSpicifyTrackList.equals(action)) {
            long albumId = WebUtil.parseLong(request.getParameter(ParamAlbumId), -1);
            int countParam = WebUtil.parseInt(request.getParameter(ParamCount), 5);
            long trackId = WebUtil.parseLong(request.getParameter(ParamTrackId), -1);

            int page = 1;

            if (albumId < 0) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("缺少albumId参数！");
            } else {
                Albums albums = new Albums();
                if (trackId > 0) {
                    Tracks tracks = new Tracks();
                    long[] trackArray = new long[1];
                    trackArray[0] = trackId;
                    int trackIndex = tracks.batchGetTracks(trackArray).get(0).getOrderNum();
                    int start = trackIndex - countParam;
                    if (start < 0) {
                        start = 0;
                    }
                    int end = trackIndex + countParam;
                    int count = 20;
                    while (start / count != end / count && count < 100) {
                        count += 10;
                    }
                    page = end / count + 1;
                    log.info("page:" + page);
                    log.info("count:" + count);
                    Paging paging = new Paging(page, count);
                    AlbumTracks albumTracks = albums.browseAlbumTracks(albumId, paging, "asc");
                    List<Track> trackList = albumTracks.getTracks();
                    List<Track> resultList = new ArrayList<Track>();
                    for (Track track : trackList) {
                        if (track.getOrderNum() >= start && track.getOrderNum() <= end) {
                            resultList.add(track);
                        }
                    }
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    result.setData(resultList);
                } else {
                    Paging paging = new Paging(page, countParam);
                    AlbumTracks albumTracks = albums.browseAlbumTracks(albumId, paging, "asc");
                    List<Track> resultList = albumTracks.getTracks();
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    result.setData(resultList);
                }

            }
        } else {
            log.error("Unsupported action : " + action);
            result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
        }

        Gson gson = GsonHelper.getGson();
        response.setContentType(WebConstants.ContentTypeText);
        PrintWriter out = response.getWriter();
        if (callback == null) {
            out.print(gson.toJson(result));
        } else {
            WebUtil.outputJSONP(out, callback, result);
        }
        out.flush();
        out.close();
    }
}
