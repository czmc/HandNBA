package me.czmc.handnba.data;

import me.czmc.handnba.data.entity.CombatData;
import me.czmc.handnba.data.entity.GameData;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by MZone on 3/22/2016.
 */
public interface NBAApi {
    String token = "cc456239f82aa28217d41af777306cd7";
    String url = "http://op.juhe.cn/onebox/basketball/";

    @GET("nba")
    Observable<GameData> getNBAInfo(@Query("key") String token);
    @GET("team")
    Observable<CombatData> getTeamInfo(@Query("key") String token,@Query("team") String team);
    @GET("combat")
    Observable<CombatData> getCombatInfo(@Query("key") String token,@Query("hteam") String hteam,@Query("vteam") String vteam);
}
