package org.dragonitemc.level.config;

import com.ericlam.mc.eld.annotations.Prefix;
import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.LangConfiguration;
import org.dragonitemc.level.api.UpdateResult;

@Prefix(path = "prefix")
@Resource(locate = "lang.yml")
public class DragonLevelMessage extends LangConfiguration {

    public String getResultMessage(UpdateResult result){
        return getLang().get("result." + result.name());
    }

    public String getErrorMessage(Throwable e) {
        e.printStackTrace();
        return getLang().get("result.ERROR", e.getMessage());
    }

}
