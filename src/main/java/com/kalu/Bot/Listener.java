package com.kalu.Bot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kalu.Bot.Data.DataSave;
import com.kalu.Bot.Data.DataSaveManager;
import com.kalu.Bot.lavaplayer.GuildMusicManager;
import com.kalu.Bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter{	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		KingGuild current = null;
		boolean guildFound = false;
		for(KingGuild g: Bot.getGuilds()) {
			if(g.checkID(event.getGuild().getIdLong())){
				current = g;
				guildFound = true;
			}
		}
		if(!guildFound) {
			KingGuild temp = new KingGuild(event.getGuild().getIdLong(),"!");
			Bot.getGuilds().add(temp);
			current = temp;
			DataSaveManager.uploadData();
		}
		if(!event.getAuthor().isBot()) {
			String args[];
			if(event.getMessage().getContentRaw().split(" ")[0].equals(current.getPrefix() + "p") || event.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(current.getPrefix() + "say")) {
				args = event.getMessage().getContentRaw().split(" ");
			}else {
				args = event.getMessage().getContentRaw().toLowerCase().split(" ");
			}
			if (args[0].contains(current.getPrefix())) {
				args[0] = args[0].replaceFirst(current.getPrefix(), "");
				switch(args[0]) {
				case "info":
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("I am king's servant,I am here to help!").queue();
					break;
				case "setprefix":
					if(event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
						current.setPrefix(args[1]);
						event.getChannel().sendMessage("Uspesno ste promenili prefix! Novi prefix je: "+ current.getPrefix()).queue();
						DataSaveManager.uploadData();
					}	
					break;
				case "kick":
					kick (event,args,current);
					break;
				case "ban":
					ban(event,args,current);
					break;	
				case "unban":
					unban(event,current);
					break;
				case "add":
					Bot.Commandsadding(event, args);
					break;
				case "addwp":
					Bot.Commandswpadding(event, args);
					break;
				case "ping":
					try {
						ping(event);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "runmega":
					if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
						try {
							Runtime.getRuntime().exec("java -jar Megabot.jar");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case "remove":
					if(event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
						Bot.remove(args[1],event.getGuild().getIdLong());
					}
					break;
				case "removewp":
					if(event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
						Bot.removewp(args[1],current);
					}
					break;
				case "addrole":
					if(event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
						switch(args[1]) {
						case "changecmd":
							current.setAddcmd(event.getMessage().getMentionedRoles().get(0));
							break;
						case "muted":
							current.setAddcmd(event.getMessage().getMentionedRoles().get(0));
							for (int i = 0; i < event.getGuild().getCategories().size(); i++) {
								event.getGuild().getCategories().get(i).getManager().putPermissionOverride(current.getMuted(), 0, Permission.ALL_PERMISSIONS).complete();
								for(GuildChannel g : event.getGuild().getCategories().get(i).getChannels()) {
									g.getManager().sync().complete();
								}
							}
							break;
						}
					}
					break;
				case "addcha":
					if(event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
						switch(args[1]) {
						case "logs":
							current.setLogsId(event.getMessage().getMentionedChannels().get(0).getIdLong());
							DataSaveManager.uploadData();
							Bot.getJda().getTextChannelById(current.getLogsId()).sendMessage("Succesfully set this channell as a logs channel").queue();
							break;
						case"join":
							current.setBfmention(event.getMessage().getContentRaw().split("<>")[0].replace((current.getPrefix()+ "addcha " + "join "+ event.getMessage().getMentionedChannels().get(0).getAsMention()), ""));
							current.setAfmention(event.getMessage().getContentRaw().split("<>")[1]);
							current.setJoinId(event.getMessage().getMentionedChannels().get(0).getIdLong());
							DataSaveManager.uploadData();
							Bot.getJda().getTextChannelById(current.getJoinId()).sendMessage("Succesfully set this channell as a join channel").queue();
							break;
						case "leave":
							current.setBfmentionleave(event.getMessage().getContentRaw().split("<>")[0].replace((current.getPrefix()+ "addcha " + "leave "+ event.getMessage().getMentionedChannels().get(0).getAsMention()), ""));
							current.setAfmentionleave(event.getMessage().getContentRaw().split("<>")[1]);
							current.setLeaveId(event.getMessage().getMentionedChannels().get(0).getIdLong());

							getChbyId(current.getJoinId(),current).sendMessage("Succesfully set this channell as a leave channel").queue();
							break;
						case "music":
							current.setMusicChannel(event.getMessage().getMentionedChannels().get(0).getIdLong());
							event.getMessage().getMentionedChannels().get(0).sendMessage("Succesfully set this channel as a music channel").queue();
							break;
						}
					}
					break;
				case "shutdown":
					if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
						Bot.getJda().shutdown();
					}
					break;
				case "clear":
					if(event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
						event.getChannel().purgeMessages(event.getChannel().getHistoryBefore(event.getMessage(), Integer.parseInt(args[1])).complete().getRetrievedHistory());
						event.getMessage().delete().complete();
					}
					break;
				case "addbword":
					if(event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
						current.getBanned().add(args[1]);
						DataSaveManager.uploadData();
					}
					break;
				case "rembword":
					if(event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
						current.getBanned().remove(args[1]);
						DataSaveManager.uploadData();
					}
					break;

				case "say":
					StringBuilder sb = new StringBuilder();
					for(int i = 1; i< args.length; i++) {
						sb.append(args[i] + " ");
					}
					event.getMessage().delete().complete();
					event.getChannel().sendMessage(sb.toString()).queue();

					break;
				default:
					for (Commands s : current.getListaKom()) {
						if(args[0].equals(s.getName())) {
							if(!(event.getMember().getUser().isBot())){
								event.getChannel().sendTyping().queue();
								event.getChannel().sendMessage(s.getCommand()).queue();
							}
						}
					}
				}
			}else {
				for(Commands s : current.getListaKomBP()) {
					for(int i =0; i<args.length; i++) {
						if(args[i].equals(s.getName())) {
							event.getChannel().sendTyping().queue();
							event.getChannel().sendMessage(s.getCommand()).queue();
						}
					}
				}
				for(int i =0; i<current.getBanned().size(); i++) {
					String s = current.getBanned().get(i);
					if(!(event.getAuthor().isBot())) {
						if(!(event.getMember().hasPermission(Permission.BAN_MEMBERS))) {
							if(event.getMessage().getContentRaw().contains(s)) {
								boolean nadjen = false;
								event.getMessage().delete().complete();
								for(Warns w : current.getWarns()) {
									if(w.getU() != null) {
										if(w.getU().getIdLong() == event.getAuthor().getIdLong()) {
											nadjen = true;
											w.warnadd("Message contained banned word(by Automoderator)");

											if(w.getNumwarned() == Bot.getBanlimit()) {
												event.getMember().ban(0, "to many infractions").complete();
												getChbyId(current.getLogsId(),current).sendMessage("Banned " +event.getMember().getAsMention());
											}else if(w.getNumwarned()==Bot.getKicklimit()) {
												event.getMember().kick("to many infractions").complete();
												getChbyId(current.getLogsId(),current).sendMessage("Kicked " +event.getMember().getAsMention());
											}else if(w.getNumwarned()==Bot.getMutelimit()) {

												event.getGuild().addRoleToMember(event.getMember(), current.getMuted()).complete();
												event.getChannel().sendMessage(event.getMember().getAsMention() + "  muted to many infractions").queue();
												getChbyId(current.getLogsId(),current).sendMessage("Muted " +event.getMember().getAsMention()).queue();
											}
											DataSaveManager.uploadData();
										}
									}
								}
								if(!nadjen) {
									current.getWarns().add(new Warns(event.getAuthor(),"Message contained banned words(by Automoderator)"));
									DataSaveManager.uploadData();
								}
								PrivateChannel pc = event.getAuthor().openPrivateChannel().complete();
								if(event.getAuthor().hasPrivateChannel()) {
									pc.sendMessage(event.getMember().getAsMention() + " You message was deleted(contains banned words)").queue();
								}else {
									event.getChannel().sendMessage(event.getMember().getAsMention() + " You message was deleted(contains banned words)").queue();
								}
								pc.close();
							}
						}
					}
				}
			}
		}
		super.onGuildMessageReceived(event);
	}
	public static TextChannel getChbyId(long id,KingGuild current) {
		return Bot.getJda().getGuildById(current.getGuild()).getTextChannelById(id);
	}
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		KingGuild current = null;
		for(KingGuild g: Bot.getGuilds()) {
			if(g.checkID(event.getGuild().getIdLong())){
				current = g;
			}
		}
		getChbyId(current.getJoinId(),current).sendMessage(current.getBfmention() + event.getMember().getAsMention() + current.getAfmention()).queue();
	}
	@Override
	public void onReady(ReadyEvent event) {
		//Bot.systoprefix();
		System.out.println("Ready!");
		DataSaveManager.downloadData();
		for(KingGuild kg : Bot.getGuilds()) {
			if(kg.getBanned() == null) {
				kg.setBanned(new ArrayList<String>());
			}
			if(kg.getListaKom() == null) {
				kg.setListaKom(new ArrayList<Commands>());
			}
			if(kg.getListaKomBP() == null) {
				kg.setListaKomBP(new ArrayList<Commands>());
			}
			if(kg.getWarns() == null) {
				kg.setWarns(new ArrayList<Warns>());
			}
		}
		DataSaveManager.uploadData();
		super.onReady(event);
	}
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		KingGuild current = null;
		for(KingGuild g: Bot.getGuilds()) {
			if(g.checkID(event.getGuild().getIdLong())){
				current = g;
			}
		}
		getChbyId(current.getLeaveId(),current).sendMessage(current.getBfmentionleave() + event.getMember().getAsMention() + current.getAfmentionleave()).queue();
		super.onGuildMemberLeave(event);
	}
	@Override
	public void onCategoryCreate(CategoryCreateEvent event) {
		KingGuild current = null;
		for(KingGuild g: Bot.getGuilds()) {
			if(g.checkID(event.getGuild().getIdLong())){
				current = g;
			}
		}
		event.getCategory().getManager().putPermissionOverride(current.getMuted(), 0, Permission.ALL_PERMISSIONS).complete();
	}
	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		event.getChannel().getManager().sync().complete();
		super.onTextChannelCreate(event);
	}
	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		event.getChannel().getManager().sync().complete();
		super.onVoiceChannelCreate(event);
	}
	public static void kick(GuildMessageReceivedEvent event,String args[],KingGuild current) {
		EmbedBuilder ebpl = new EmbedBuilder();
		ebpl.setColor(new Color(178,30,30));
		ebpl.setTitle("Error ocurred");
		if(args.length >= 2) {
			EmbedBuilder ebtrue = new EmbedBuilder();
			ebtrue.setColor(new Color(54,138,40));
			ebtrue.setTitle("Succesfully kicked " + event.getMessage().getMentionedUsers().get(0).getAsTag());
			ebtrue.addField("Kicked user ", event.getMessage().getMentionedMembers().get(0).getAsMention(), false);
			ebtrue.addField("Reason",args[2],false);
			ebtrue.addField("Moderator", event.getMember().getAsMention(), false);
			if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				if(event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.ADMINISTRATOR)){
					ebpl.addField("","cant kick same role",false);
					event.getChannel().sendMessage(ebpl.build()).queue();
				}else {
					event.getMessage().getMentionedMembers().get(0).kick(args[2]).complete();
					getChbyId(current.getLogsId(),current).sendMessage(ebtrue.build()).queue();
				}
			}else if(event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
				if(event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.MANAGE_ROLES)){
					ebpl.addField("","cant kick same role",false);
					event.getChannel().sendMessage(ebpl.build()).queue();
				}else {
					event.getMessage().getMentionedMembers().get(0).kick(args[2]).complete();
					getChbyId(current.getLogsId(),current).sendMessage(ebtrue.build()).queue();
				}
			}else if(event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
				if(event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.KICK_MEMBERS)){
					ebpl.addField("","cant kick same role",false);
					event.getChannel().sendMessage(ebpl.build()).queue();
				}else {
					event.getMessage().getMentionedMembers().get(0).kick(args[2]).complete();
					getChbyId(current.getLogsId(),current).sendMessage(ebtrue.build()).queue();
				}
			}else {
				ebpl.addField("","you dont have permission to kick",false);
				event.getChannel().sendMessage(ebpl.build()).queue();
			}
		}else {
			ebpl.addField("","usage " + current.getPrefix() + "kick <member> <reason>",false);
			event.getChannel().sendMessage(ebpl.build()).queue();
		}
	}
	public static void ban(GuildMessageReceivedEvent event,String args[],KingGuild current) {
		EmbedBuilder ebpl = new EmbedBuilder();
		ebpl.setColor(new Color(178,30,30));
		ebpl.setTitle("Error ocurred");

		if(args.length >2) {
			EmbedBuilder ebtrue = new EmbedBuilder();
			ebtrue.setColor(new Color(54,138,40));
			ebtrue.setTitle("Succesfully banned " + event.getMessage().getMentionedUsers().get(0).getAsTag());
			ebtrue.addField("Banned user ", event.getMessage().getMentionedMembers().get(0).getAsMention(), false);
			ebtrue.addField("Reason",args[3],false);
			ebtrue.addField("Moderator", event.getMember().getAsMention(), false);
			if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				if(event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.ADMINISTRATOR)){
					ebpl.addField("","cant ban same role",false);
					event.getChannel().sendMessage(ebpl.build()).queue();
				}else {
					event.getMessage().getMentionedMembers().get(0).ban(0,args[2]).complete();
					getChbyId(current.getLogsId(),current).sendMessage(ebtrue.build()).queue();

				}
			}else if(event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
				if(event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.MANAGE_ROLES)){
					ebpl.addField("","cant ban same role",false);
					event.getChannel().sendMessage(ebpl.build()).queue();
				}else {
					event.getMessage().getMentionedMembers().get(0).ban(0,args[2]).complete();;
					getChbyId(current.getLogsId(),current).sendMessage(ebtrue.build()).queue();

				}
			}else if(event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
				if(event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.BAN_MEMBERS)){
					ebpl.addField("","cant ban same role",false);
					event.getChannel().sendMessage(ebpl.build()).queue();
				}else {

					event.getMessage().getMentionedMembers().get(0).ban(0, args[2]).complete();
					getChbyId(current.getLogsId(),current).sendMessage(ebtrue.build()).queue();

				}
			}else {
				ebpl.addField("","you have no permission to ban",false);
				event.getChannel().sendMessage(ebpl.build()).queue();
			}
		}else {
			ebpl.addField("","Usage !ban <member> <reason>",false);
			event.getChannel().sendMessage(ebpl.build()).queue();
		}
	}
	public static void unban(GuildMessageReceivedEvent event,KingGuild current) {
		String id = event.getMessage().getContentRaw().split("<")[1].split(">")[0].replace("!", "").replace("@", "");
		EmbedBuilder eb = new EmbedBuilder();
		if(event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			eb.setColor(new Color(54,138,40));
			event.getGuild().unban(id).complete();
			eb.setTitle("You succesfully unbanned " + Bot.getJda().getUserById(id).getAsTag());
			getChbyId(current.getLogsId(),current).sendMessage(eb.build()).queue();
		}else {
			eb.setColor(new Color(178,30,30));
			eb.setTitle("You don't have permission to ban");
			getChbyId(current.getLogsId(),current).sendMessage(eb.build()).queue();
		}
	}
	static void ping(GuildMessageReceivedEvent event) throws Exception {  


		Process process = Runtime.getRuntime().exec("ping discord.com");
		BufferedReader input = new BufferedReader(new InputStreamReader (process.getInputStream())); 
		String s = null;
		int i = 0;
		while ((s = input.readLine()) != null) {
			if((i != 0) && (i!=6) ) {
				event.getChannel().sendMessage(s).queue();
			}
			i++;
		}

	}
}