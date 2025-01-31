package me.cortex.voxy.client.terrain;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.cortex.voxy.client.core.IGetVoxelCore;
import me.cortex.voxy.client.importers.WorldImporter;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import java.io.File;


public class WorldImportCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return ClientCommandManager.literal("voxy").then(
                ClientCommandManager.literal("import")
                        .then(ClientCommandManager.literal("world")
                                .then(ClientCommandManager.argument("world_name", StringArgumentType.string()).executes(WorldImportCommand::importWorld)))
                        .then(ClientCommandManager.literal("bobby")
                                .then(ClientCommandManager.argument("world_name", StringArgumentType.string()).executes(WorldImportCommand::importBobby)))
                        .then(ClientCommandManager.literal("raw")
                                .then(ClientCommandManager.argument("path", StringArgumentType.string()).executes(WorldImportCommand::importRaw))));
    }

    public static WorldImporter importerInstance;

    private static int importRaw(CommandContext<FabricClientCommandSource> ctx) {
        var instance = MinecraftClient.getInstance();
        var file = new File(ctx.getArgument("path", String.class));
        importerInstance = ((IGetVoxelCore)instance.worldRenderer).getVoxelCore().createWorldImporter(MinecraftClient.getInstance().player.clientWorld, file);
        return 0;
    }

    private static int importBobby(CommandContext<FabricClientCommandSource> ctx) {
        var instance = MinecraftClient.getInstance();
        var file = new File(".bobby").toPath().resolve(ctx.getArgument("world_name", String.class)).toFile();
        importerInstance = ((IGetVoxelCore)instance.worldRenderer).getVoxelCore().createWorldImporter(MinecraftClient.getInstance().player.clientWorld, file);
        return 0;
    }

    private static int importWorld(CommandContext<FabricClientCommandSource> ctx) {
        var instance = MinecraftClient.getInstance();
        var file = new File("saves").toPath().resolve(ctx.getArgument("world_name", String.class)).resolve("region").toFile();
        importerInstance = ((IGetVoxelCore)instance.worldRenderer).getVoxelCore().createWorldImporter(MinecraftClient.getInstance().player.clientWorld, file);
        return 0;
    }

}