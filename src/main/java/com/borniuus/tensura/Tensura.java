package com.borniuus.tensura;

import com.borniuus.tensura.data.TensuraBlockStateProvider;
import com.borniuus.tensura.data.TensuraItemModelProvider;
import lombok.Getter;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Tensura.MOD_ID)
public class Tensura {
    public static final String MOD_ID = "tensura";
    private static final Logger LOGGER = LogManager.getLogger();
    @Getter
    private final TensuraCommon proxy;

    public Tensura() {
        proxy = DistExecutor.safeRunForDist(() -> TensuraClient::new, () -> TensuraCommon::new);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        proxy.preInit(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::generateData);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(proxy::clientInit);
    }

    private void generateData(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        event.getGenerator().addProvider(new TensuraBlockStateProvider(generator, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new TensuraItemModelProvider(generator, event.getExistingFileHelper()));
    }

    /**
     * Public Getter for the Logger instance of this mod.
     *
     * @return the current Logger instance
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}


