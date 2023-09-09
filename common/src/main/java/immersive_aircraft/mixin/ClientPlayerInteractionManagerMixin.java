package immersive_aircraft.mixin;

import immersive_aircraft.entity.InventoryVehicleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "isServerControlledInventory()Z", at = @At("HEAD"), cancellable = true)
    public void hasRidingInventory(CallbackInfoReturnable<Boolean> cir) {
        assert minecraft.player != null;
        if (minecraft.player.isPassenger() && minecraft.player.getVehicle() instanceof InventoryVehicleEntity) {
            cir.setReturnValue(true);
        }
    }
}
