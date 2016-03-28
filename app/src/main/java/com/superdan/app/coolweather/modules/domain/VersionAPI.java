package com.superdan.app.coolweather.modules.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *{
 "name": "酷酷天气",
 "version": "1",
 "changelog": "First release",
 "updated_at": 1459139551,
 "versionShort": "1.0",
 "build": "1",
 "installUrl": "http://download.fir.im/v2/app/install/56f8b3b900fc7466fb000016?download_token=98b7760c9613293fe734bfa9ee7ef15c",
 "install_url": "http://download.fir.im/v2/app/install/56f8b3b900fc7466fb000016?download_token=98b7760c9613293fe734bfa9ee7ef15c",
 "direct_install_url": "http://download.fir.im/v2/app/install/56f8b3b900fc7466fb000016?download_token=98b7760c9613293fe734bfa9ee7ef15c",
 "update_url": "http://fir.im/coolweather",
 "binary": {
 "fsize": 4201996
 }
 }
 */


/**
 * Created by dsz on 16/3/16.
 */
public class VersionAPI implements Serializable{
        @SerializedName("name") public  String name;
        @SerializedName("version") public String version;
        @SerializedName("changelog")public String changelog;
        @SerializedName("updated_at")public long  updatedAt;
        @SerializedName("versionShort")public String versionShort;
        @SerializedName("build")public  String build;
        @SerializedName("installUrl")public String instanllUrl;
        @SerializedName("direct_install_url")public String directInstallUrl;
        @SerializedName("update_url")public String updateUrl;

        @SerializedName("binary")public BinaryEntity binaryEntity;
        public class BinaryEntity{
                @SerializedName("fsize") public long fsize;
        }


}
