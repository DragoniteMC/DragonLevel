package org.dragonitemc.level;

import chu77.eldependenci.sql.SQLInstallation;
import com.ericlam.mc.eld.BukkitManagerProvider;
import com.ericlam.mc.eld.ELDBukkit;
import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ServiceCollection;
import org.dragonitemc.level.api.AsyncLevelService;
import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.dragonitemc.level.config.DragonLevelMessage;
import org.dragonitemc.level.db.Levels;
import org.dragonitemc.level.manager.AsyncLevelManager;
import org.dragonitemc.level.manager.LevelManager;
import org.dragonitemc.level.repository.LevelRepository;

@ELDBukkit(
    lifeCycle = DragonLevelLifeCycle.class,
    registry = DragonLevelRegistry.class
)
public class DragonLevel extends ELDBukkitPlugin {

    @Override
    public void bindServices(ServiceCollection collection) {

        collection.addConfiguration(DragonLevelConfig.class);
        collection.addConfiguration(DragonLevelMessage.class);

        collection.bindService(LevelService.class, LevelManager.class);
        collection.bindService(AsyncLevelService.class, AsyncLevelManager.class);

        SQLInstallation sqlInstallation = collection.getInstallation(SQLInstallation.class);
        sqlInstallation.bindEntities(Levels.class);
        sqlInstallation.bindJpaRepository(LevelRepository.class);

    }

    @Override
    protected void manageProvider(BukkitManagerProvider provider) {

    }

}
