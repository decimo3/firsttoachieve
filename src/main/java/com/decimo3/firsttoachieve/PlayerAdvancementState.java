package com.decimo3.firsttoachieve;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class PlayerAdvancementState extends PersistentState {

    private final Set<String> claimedAdvancements = new HashSet<>();

    public static PlayerAdvancementState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                PlayerAdvancementState::fromNbt,
                PlayerAdvancementState::new,
                "first2achieve_advancements"
        );
    }

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList list = new NbtList();

        for (String id : claimedAdvancements) {
            list.add(NbtString.of(id));
        }

        nbt.put("claimed", list);
        return nbt;
	}

    public static PlayerAdvancementState fromNbt(NbtCompound nbt) {
        PlayerAdvancementState state = new PlayerAdvancementState();

        NbtList list = nbt.getList("claimed", 8);

        for (int i = 0; i < list.size(); i++) {
            state.claimedAdvancements.add(list.getString(i));
        }

        return state;
    }

    public boolean isClaimed(String id) {
        return claimedAdvancements.contains(id);
    }

    public void claim(String id) {
        claimedAdvancements.add(id);
        markDirty();
    }

}
