package com.kalu.Bot;

import com.kalu.Bot.lavaplayer.GuildMusicManager;
import com.kalu.Bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TempMusicListener extends ListenerAdapter{
	TextChannel channel;
	AudioPlaylist playlist;
	Message message;
	public TempMusicListener(TextChannel channel,AudioPlaylist playlist,Message message) {
		this.channel = channel;
		this.playlist = playlist;
		this.message= message;
	}
	public TempMusicListener() {
		channel = null;
	}
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getChannel() == channel) {
			for(int i = 0; i < event.getMessage().getContentRaw().length(); i++) {
				if(!Character.isDigit(event.getMessage().getContentRaw().charAt(i))) {
					return;
				}
			}
			int choose = Integer.parseInt(event.getMessage().getContentRaw().split(" ")[0]);
		final GuildMusicManager musicManager = PlayerManager.GetInstance().getMusicManager(channel.getGuild());
		if(choose==0) {
			event.getMessage().delete().queue();
			message.delete().queue();
			Bot.getJda().removeEventListener(this);
		}
		if(!(choose > 10)) {
		AudioTrack track = playlist.getTracks().get(Integer.parseInt((event.getMessage().getContentRaw().split(" ")[0]))-1);
		musicManager.scheduler.queue(track);
		channel.sendMessage("Adding to queue: '")
		.append(track.getInfo().title)
		.append("' by '")
		.append(track.getInfo().author)
		.append("' ")
		.queue();
		event.getMessage().delete().queue();
		message.delete().queue();
		Bot.getJda().removeEventListener(this);
		}
		}
		super.onGuildMessageReceived(event);
	}
}
