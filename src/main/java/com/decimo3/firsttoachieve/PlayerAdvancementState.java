package com.decimo3.firsttoachieve;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class PlayerAdvancementState extends PersistentState {

	public record AdvancementRecord(long timestamp, String playerName, String advancementId) {
	}

	private final List<AdvancementRecord> records = new ArrayList<>();

	public static PlayerAdvancementState get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(
				PlayerAdvancementState::fromNbt,
				PlayerAdvancementState::new,
				FirstToAchieve.MOD_ID);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList list = new NbtList();

		for (AdvancementRecord record : records) {
			NbtCompound entry = new NbtCompound();

			entry.putLong("timestamp", record.timestamp());
			entry.putString("player", record.playerName());
			entry.putString("advancement", record.advancementId());

			list.add(entry);
		}

		nbt.put("claimed", list);
		return nbt;
	}

	public static PlayerAdvancementState fromNbt(NbtCompound nbt) {
		PlayerAdvancementState state = new PlayerAdvancementState();

		NbtList list = nbt.getList("claimed", 10);

		for (int i = 0; i < list.size(); i++) {
			NbtCompound entry = list.getCompound(i);
			state.records.add(new AdvancementRecord(
					entry.getLong("timestamp"),
					entry.getString("player"),
					entry.getString("advancement")));
		}

		return state;
	}

	public boolean isClaimed(String id) {
		return records.stream().anyMatch(record -> record.advancementId.equals(id));
	}

	public void claim(String playerName, String AdvancementId) {
		records.add(new AdvancementRecord(
				System.currentTimeMillis(), playerName, AdvancementId));
		markDirty();
	}

	public List<AdvancementRecord> getAdvancements() {
		return records;
	}
}
