package immersive_aircraft.config;

import immersive_aircraft.Main;
import immersive_aircraft.config.configEntries.BooleanConfigEntry;
import immersive_aircraft.config.configEntries.FloatConfigEntry;
import immersive_aircraft.config.configEntries.IntegerConfigEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ConfigScreen {
    public static Screen getScreen() {
        Config config = Config.getInstance();

        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new TranslatableText("itemGroup." + Main.MOD_ID + "." + Main.MOD_ID + "_tab"))
                .setSavingRunnable(config::save);

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("option." + Main.MOD_ID + ".general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // regular fields
        for (Field field : Config.class.getDeclaredFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                try {
                    String key = "option." + Main.MOD_ID + "." + field.getName();
                    if (annotation instanceof IntegerConfigEntry) {
                        IntegerConfigEntry entry = (IntegerConfigEntry)annotation;
                        general.addEntry(entryBuilder.startIntField(new TranslatableText(key), field.getInt(config))
                                .setDefaultValue(entry.value())
                                .setSaveConsumer(v -> {
                                    try {
                                        field.setInt(config, v);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .setMin(entry.min())
                                .setMax(entry.max())
                                .build());
                    } else if (annotation instanceof FloatConfigEntry) {
                        FloatConfigEntry entry = (FloatConfigEntry)annotation;
                        general.addEntry(entryBuilder.startFloatField(new TranslatableText(key), field.getFloat(config))
                                .setDefaultValue(entry.value())
                                .setSaveConsumer(v -> {
                                    try {
                                        field.setFloat(config, v);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .setMin(entry.min())
                                .setMax(entry.max())
                                .build());
                    } else if (annotation instanceof BooleanConfigEntry) {
                        BooleanConfigEntry entry = (BooleanConfigEntry)annotation;
                        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText(key), field.getBoolean(config))
                                .setDefaultValue(entry.value())
                                .setSaveConsumer(v -> {
                                    try {
                                        field.setBoolean(config, v);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .build());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return builder.build();
    }
}
