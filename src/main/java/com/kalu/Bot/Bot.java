package com.kalu.Bot;

import java.util.List;
import java.util.Scanner;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.security.auth.login.LoginException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalu.Bot.Data.DataSaveManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.internal.entities.EntityBuilder;


public class Bot{
	private static int mutelimit = 10;
	private static int kicklimit = 20;
	private static int banlimit =  30;	

	private static List<KingGuild> Guilds = new ArrayList<KingGuild>();
	private static JDA jda;
	private static Gson gson;
	public static void main(String[] args) throws LoginException{
		GsonBuilder builder = new GsonBuilder();
		gson = builder
				.setPrettyPrinting()
				.excludeFieldsWithoutExposeAnnotation()
				.create();
		builder = null;
		jda = JDABuilder.createDefault(token2).build();
				
		jda.getPresence().setPresence(OnlineStatus.IDLE, EntityBuilder.createActivity(" a lot of games",null , ActivityType.DEFAULT));
		jda.addEventListener(new Listener());
		jda.addEventListener(new MusicListener());
	}
	public static JDA getJda() {
		return jda;
	}
	public static void setJda(JDA jda) {
		Bot.jda = jda;
	}
	public static int getMutelimit() {
		return mutelimit;
	}
	public static void setMutelimit(int mutelimit) {
		Bot.mutelimit = mutelimit;
	}
	public static int getKicklimit() {
		return kicklimit;
	}
	public static void setKicklimit(int kicklimit) {
		Bot.kicklimit = kicklimit;
	}
	public static Gson getGson() {
		return gson;
	}
	public static void setGson(Gson gson) {
		Bot.gson = gson;
	}
	public static void Commandsadding(GuildMessageReceivedEvent event,String[]args) {
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
		}
		if(event.getMember().getRoles().contains(current.getAddcmd()) || event.getMember().hasPermission(Permission.KICK_MEMBERS)){
			if(!(args[1].isEmpty()) || !(args[2].isEmpty())) { 
				String name = event.getMessage().getContentRaw().split("<>")[0].replace((current.getPrefix() + "add "), "");
				String command = event.getMessage().getContentRaw().split("<>")[1];
				current.getListaKom().add(new Commands(name, command));
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(new Color(54, 138, 40));
				eb.setTitle("Added new Command ");
				eb.addField("Command is "+ name," Command output is " + command,false);
				for(int i = 0; i< current.getListaKom().size(); i++) {
					Commands s = current.getListaKom().get(i);
					eb.addField( (i+1) + ". Command is "+ s.getName()," Command output is " + s.getCommand(),false);
				}
				event.getChannel().sendMessage(eb.build()).queue();
				DataSaveManager.uploadData();
				
			}
		}else {
			event.getChannel().sendMessage("You don't have permission to add commands").queue();
		}
}
	public static void Commandswpadding(GuildMessageReceivedEvent event,String[]args) {
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
		}
		if(event.getMember().getRoles().contains(current.getAddcmd()) || event.getMember().hasPermission(Permission.KICK_MEMBERS)){
			if(!(args[1].isEmpty()) || !(args[2].isEmpty())) { 
				String name = event.getMessage().getContentRaw().split("<>")[0].replace(current.getPrefix() + "addwp ", "");
				String command = event.getMessage().getContentRaw().split("<>")[1];
				current.getListaKomBP().add(new Commands(name, command));
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(new Color(54, 138, 40));
				eb.setTitle("Added new Command ");
				eb.addField("Command is "+ name," Command output is " + command,false);
				for(int i = 0; i< current.getListaKomBP().size(); i++) {
					Commands s = current.getListaKomBP().get(i);
					eb.addField( (i+1) + ". Command is "+ s.getName()," Command output is " + s.getCommand(),false);
				}
				event.getChannel().sendMessage(eb.build()).queue();
				DataSaveManager.uploadData();
			}
		}else {
			event.getChannel().sendMessage("You don't have permission to add commands").queue();
		}
	}
	public static void remove(String args,long id){
		KingGuild current = null;
		for( KingGuild kg : Bot.Guilds) {
			if(kg.checkID(id)) {
				current = kg;
			}
		}

		for(Commands s : current.getListaKom()) {
			if(s.Searchbyname(args)) {
				current.getListaKom().remove(s);
			}
			DataSaveManager.uploadData();
		}
	}
	public static void removewp(String name,KingGuild current){
		Commands remove = null;
		for(Commands s : current.getListaKomBP()) {
			if(s.Searchbyname(name)) {
				remove = s;
			}
		}
		current.getListaKomBP().remove(remove);
		DataSaveManager.uploadData();
	}
	public static int getBanlimit() {
		return banlimit;
	}
	public static void setBanlimit(int banlimit) {
		Bot.banlimit = banlimit;
	}

	public static List<KingGuild> getGuilds() {
		return Guilds;
	}

	public static void setGuilds(List<KingGuild> guilds) {
		Guilds = guilds;
	}
}









