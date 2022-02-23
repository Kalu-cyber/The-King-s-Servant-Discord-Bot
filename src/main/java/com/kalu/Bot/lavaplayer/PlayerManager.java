package com.kalu.Bot.lavaplayer;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kalu.Bot.Bot;
import com.kalu.Bot.TempMusicListener;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
	private static PlayerManager INSTANCE;
	private final Map<Long, GuildMusicManager> musicManagers;
	private final AudioPlayerManager audioPlayerManager;
	
	public PlayerManager() {
		this.musicManagers = new HashMap<>();
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		
		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
	}
	
	public GuildMusicManager getMusicManager(Guild guild) {
		return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
			guild.getAudioManager().setSendingHandler(guildMusicManager.getSendhandler());
			return guildMusicManager;
		});
	}
	
	public void loadAndPlay(TextChannel channel, String trackUrl) {
		final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
		
		this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				musicManager.scheduler.queue(track);
				channel.sendMessage("Adding to queue: '")
				.append(track.getInfo().title)
				.append("' by '")
				.append(track.getInfo().author)
				.append("' ")
				.queue();
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				if(playlist.isSearchResult()) {
				final List<AudioTrack> tracks = playlist.getTracks();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(new Color(35,36,255));
				eb.setTitle("Search results ");
				for(int i = 0; i<10; i++) {
					AudioTrack track = tracks.get(i);
					eb.addField((i+1) + ". "+ track.getInfo().author+"-" + track.getInfo().title,"", false);
				}
				eb.addField("0.exit","",false);
				Bot.getJda().addEventListener(new TempMusicListener(channel, playlist,channel.sendMessage(eb.build()).complete()));
				}else {
					
				}
			}
			
			@Override
			public void noMatches() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public static PlayerManager GetInstance(){
		if(INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}
		return INSTANCE;
	}
	
}
