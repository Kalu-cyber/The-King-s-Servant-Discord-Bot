package com.kalu.Bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public class Warns {
	@Expose(serialize = true,deserialize = true)@SerializedName("user")
	private  long u;
	@Expose(serialize = true,deserialize = true)@SerializedName("num")
	private  int numwarned = 0;
	@Expose(serialize = true,deserialize = true)@SerializedName("reasons")
	private  List<String> reasons = new ArrayList<>();
	public Warns(User u,String reason) {
		this.u = u.getIdLong();
		this.numwarned ++;
		this.reasons.add(reason);
	}
	public Warns(User u,int numwarned,List<String> reasons2) {
		if(u!= null) {
			this.u = u.getIdLong();
		}
		this.numwarned = numwarned;
		this.reasons = reasons2;
	}
	public  User getU() {
		return Bot.getJda().retrieveUserById(u).complete();
	}
	public  void setU(User u) {
		this.u = u.getIdLong();
	}
	public long getUid() {
		return u;
	}
	public  int getNumwarned() {
		return numwarned;
	}
	public  void setNumwarned(int numwarned) {
		this.numwarned = numwarned;
	}
	public  List<String> getReasons() {
		return reasons;
	}
	public void setReasons(List<String> reasons) {
		this.reasons = reasons;
	}
	public  void removeallinf() {
		numwarned = 0;
		this.reasons.clear();
	}
	public void removeone(int num) {
		this.reasons.remove(num);
		this.numwarned --;
	}
	public void warnadd(String reason) {
		numwarned++;
		reasons.add(reason);
	}

}
