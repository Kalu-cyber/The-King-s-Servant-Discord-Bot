package com.kalu.Bot.Data;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kalu.Bot.Bot;
import com.kalu.Bot.KingGuild;

public class DataSave {
	@Expose(serialize = true,deserialize = true)@SerializedName("guilds")
	private List<KingGuild> guilds;

	public void getGuilds() {
		Bot.setGuilds(guilds);
	}

	public void setGuilds() {
		this.guilds = Bot.getGuilds();
	}
}
