package com.kalu.Bot;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import com.kalu.Bot.lavaplayer.GuildMusicManager;
import com.kalu.Bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MusicListener extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		KingGuild current = null;
		boolean guildFound = false;
		TextChannel channel = null;
		for(KingGuild mc : Bot.getGuilds()) {
			if(mc.checkID(event.getGuild().getIdLong())) {
				guildFound = true;
				channel = Bot.getJda().getTextChannelById(mc.getMusicChannel());
				current = mc;
			}
		}
		if(!event.getAuthor().isBot()) {
			String args[];
			if(event.getMessage().getContentRaw().split(" ")[0].equals(current.getPrefix() + "p")) {
				args = event.getMessage().getContentRaw().split(" ");
			}else {
			args = event.getMessage().getContentRaw().toLowerCase().split(" ");
			}
			if (args[0].contains(current.getPrefix())) {
				args[0] = args[0].replace(current.getPrefix(), "");
		switch(args[0]) {
			case "join":
				if(!guildFound) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(172, 30, 30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "You need to set the music channel", false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
					}
				if(channel != event.getChannel()) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "Music can only be ordered in "+ channel.getAsMention(), false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
				}
				if(event.getMember().getVoiceState().inVoiceChannel()){
					if(event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
						if(event.getMember().getVoiceState().getChannel().equals(event.getGuild().getSelfMember().getVoiceState().getChannel())) {
							event.getChannel().sendMessage("We are already in the same channel").queue();
						}else {
							event.getChannel().sendMessage("I am busy").queue();
						}
					}else {
						Bot.getJda().getDirectAudioController().connect(event.getMember().getVoiceState().getChannel());
					}
				}else {
					event.getChannel().sendMessage("You need to be in a voice channel").queue();
				}
				break;
			case "leave":
				if(!guildFound) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "You need to set the music channel", false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
					}
				if(channel != event.getChannel()) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "Music can only be ordered in "+ channel.getAsMention(), false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
				}
					if(event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
		
						if(event.getMember().getVoiceState().inVoiceChannel()) {
							if(event.getMember().getVoiceState().getChannel().equals(event.getGuild().getSelfMember().getVoiceState().getChannel())) {
									Bot.getJda().getDirectAudioController().disconnect(event.getGuild());
							}
						}else if(event.getMember().getVoiceState().getChannel().getMembers().size() == 1) {
							Bot.getJda().getDirectAudioController().disconnect(event.getGuild());
						}else {
							event.getChannel().sendMessage("there is already someone in a voice channel").queue();
						}
					}else {
						event.getChannel().sendMessage("I am not even in a channel");
					}
					
			break;
			case "loop":
				if(PlayerManager.GetInstance().getMusicManager(event.getGuild()).scheduler.isLoop()) {
					PlayerManager.GetInstance().getMusicManager(event.getGuild()).scheduler.setLoop(false);
					event.getChannel().sendMessage(""+PlayerManager.GetInstance().getMusicManager(event.getGuild()).scheduler.isLoop()).queue();
				}else {
					PlayerManager.GetInstance().getMusicManager(event.getGuild()).scheduler.setLoop(true);
					event.getChannel().sendMessage(""+PlayerManager.GetInstance().getMusicManager(event.getGuild()).scheduler.isLoop()).queue();
				}
				break;
			case "stop":
				if(!guildFound) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "You need to set the music channel", false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
					}
				if(channel != event.getChannel()) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "Music can only be ordered in "+ channel.getAsMention(), false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
				}
				if(event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
		
					if(event.getMember().getVoiceState().inVoiceChannel()) {
						if(event.getMember().getVoiceState().getChannel().equals(event.getGuild().getSelfMember().getVoiceState().getChannel())) {
								final GuildMusicManager musicManager = PlayerManager.GetInstance().getMusicManager(event.getGuild());
								musicManager.scheduler.player.stopTrack();
								musicManager.scheduler.queue.clear();
						}
					}else if(event.getMember().getVoiceState().getChannel().getMembers().size() == 1) {
						final GuildMusicManager musicManager = PlayerManager.GetInstance().getMusicManager(event.getGuild());
						musicManager.scheduler.player.stopTrack();
						musicManager.scheduler.queue.clear();
					}else {
						event.getChannel().sendMessage("there is already someone in a voice channel").queue();
					}
				}else {
					event.getChannel().sendMessage("I am not even in a channel");
				}
			break;
			case "skip":
				if(!guildFound) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "You need to set the music channel", false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
					}
				if(channel != event.getChannel()) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "Music can only be ordered in "+ channel.getAsMention(), false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
				}
				if(event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
		
					if(event.getMember().getVoiceState().inVoiceChannel()) {
						if(event.getMember().getVoiceState().getChannel().equals(event.getGuild().getSelfMember().getVoiceState().getChannel())) {
								final GuildMusicManager musicManager = PlayerManager.GetInstance().getMusicManager(event.getGuild());
								final AudioPlayer player = musicManager.audioPlayer;
								if (!(player.getPlayingTrack() == null)) {
									musicManager.scheduler.nextTrack();
								}
						}
					}else if(event.getMember().getVoiceState().getChannel().getMembers().size() == 1) {
						final GuildMusicManager musicManager = PlayerManager.GetInstance().getMusicManager(event.getGuild());
						final AudioPlayer player = musicManager.audioPlayer;
						if (!(player.getPlayingTrack() == null)) {
							musicManager.scheduler.nextTrack();
						}
						
					}else {
						event.getChannel().sendMessage("there is already someone in a voice channel").queue();
					}
				}else {
					event.getChannel().sendMessage("I am not even in a channel");
				}
				break;
			case "p":
				if(!guildFound) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "You need to set the music channel", false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
					}
				if(channel != event.getChannel()) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: Error ocurred");
					eb.addField("An error ocurred", "Music can only be ordered in "+ channel.getAsMention(), false);
					event.getChannel().sendMessage(eb.build()).queue();
					break;
				}
				if(!(args.length < 2)) {
				if(event.getMember().getVoiceState().inVoiceChannel()){
					if(event.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
						if(event.getMember().getVoiceState().getChannel().equals(event.getGuild().getSelfMember().getVoiceState().getChannel())) {
							String link;
							if(IsUrl(args[1])) {
								link = args[1];
							}else {
								link = "ytsearch:" + event.getMessage().getContentRaw().replace(args[0], "");
							}
							PlayerManager.GetInstance().loadAndPlay(event.getChannel(), link);
						}else {
							event.getChannel().sendMessage("We need to be in a same channel to work").queue();
						}
					}else {
						Bot.getJda().getDirectAudioController().connect(event.getMember().getVoiceState().getChannel());
						String link;
						if(IsUrl(args[1])) {
							link = args[1];
						}else {
							link = "ytsearch:" + event.getMessage().getContentRaw().replace(args[0], "");
						}
						PlayerManager.GetInstance().loadAndPlay(event.getChannel(), link);
					}
				}else {
					event.getChannel().sendMessage("you need to be in a voice channel").queue();
					break; 
				}
				}else {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(178,30,30));
					eb.setTitle(":red_square: **An error occured**");
					eb.addField("Wrong usage", "Usage: " + current.getPrefix() + "p <Url,song name>",false);
					event.getChannel().sendMessage(eb.build()).queue();
				}
				event.getMessage().delete().queue();
			break;	
				}
	}
	}
		super.onGuildMessageReceived(event);
	}
	
	public static boolean IsUrl(String Url) {

		try {
			new URL(Url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
		

}
}
