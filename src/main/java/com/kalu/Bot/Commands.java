package com.kalu.Bot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commands {
	@Expose(serialize = true,deserialize = true)@SerializedName("name")
	 private  String Name;
	@Expose(serialize = true,deserialize = true)@SerializedName("command")
	 private  String command;
	 
	 Commands(String name, String command){
		 this.Name=name;
		 this.command= command;
	 }
	 
	public Commands() {
		Name = "";
		command = "";
	}

	public String getName() {
		return Name;
	}

	public  void setName(String name) {
		Name = name;
	}

	public  String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	public boolean Searchbyname(String name) {
		boolean ret = false;
		if(this.Name.equalsIgnoreCase(name))
			ret=true;
		return ret;
	}
	public  Commands getcmdfromstrind(String cmd ) {
		String name;
		String Command;
		name = cmd.split("<>")[0].replace("name:", "");
		Command = cmd.split("<>")[1].replace("command:", "");
		return new Commands(name,Command);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name:");
		sb.append(this.Name);
		sb.append("<>command:");
		sb.append(this.command);
		return sb.toString();
	}
}
