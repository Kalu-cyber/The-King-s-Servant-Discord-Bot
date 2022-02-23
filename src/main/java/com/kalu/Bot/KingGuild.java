package com.kalu.Bot;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class KingGuild {
	@Expose@SerializedName("musicChannel")
	 private long musicChannel;
	@Expose@SerializedName("guild")
	 private long guildId;
	@Expose@SerializedName("prefix")
	 private String prefix;
	@Expose@SerializedName("commands")
	private List<Commands> listaKom = new ArrayList<Commands>();
	@Expose@SerializedName("commandswp")
	private List<Commands> listaKomBP = new ArrayList<Commands>();
	@Expose@SerializedName("joinbf")
	private String bfmention;
	@Expose@SerializedName("joinaf")
	private  String afmention;
	@Expose@SerializedName("banwords")
	private List<String> banned = new ArrayList<String>();
	@Expose@SerializedName("leavebf")
	private String bfmentionleave;
	@Expose@SerializedName("leaveaf")
	private String afmentionleave;
	@Expose@SerializedName("logs")
	private long logsId;
	@Expose@SerializedName("join")
	private long JoinId;
	@Expose@SerializedName("leave")
	private long leaveId;
	@Expose@SerializedName("addcmd")
	private long addcmd;
	@Expose@SerializedName("muted")
	private long muted;
	@Expose@SerializedName("warns")
	private List<Warns> warns= new ArrayList<>();
	 public KingGuild(long musicChannel, long guildId, String prefix) {
		super();
		this.musicChannel = musicChannel;
		this.guildId = guildId;
		this.setPrefix(prefix);
	}

	public KingGuild(long guildId,long channel) {
		this.guildId = guildId;
		this.musicChannel = channel;
		setPrefix("!");
	}
	 
	public KingGuild(long guildId,String prefix) {
		this.guildId = guildId;
		this.setPrefix(prefix);
	}
	public long getMusicChannel() {
		return musicChannel;
	}

	public void setMusicChannel(long musicChannel) {
		this.musicChannel = musicChannel;
	}

	public long getGuild() {
		return guildId;
	}
	public void setGuild(long guild) {
		this.guildId = guild;
	}

	
	public boolean checkID(Long id) {
		if(this.guildId == id) {
			return true;
		}else {
			return false;
		}
	}
	public  String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<Commands> getListaKom() {
		return listaKom;
	}

	public void setListaKom(List<Commands> listaKom) {
		this.listaKom = listaKom;
	}

	public List<Commands> getListaKomBP() {
		return listaKomBP;
	}

	public void setListaKomBP(List<Commands> listaKomBP) {
		this.listaKomBP = listaKomBP;
	}

	public String getAfmention() {
		return afmention;
	}

	public void setAfmention(String afmention) {
		this.afmention = afmention;
	}

	public String getBfmention() {
		return bfmention;
	}

	public void setBfmention(String bfmention) {
		this.bfmention = bfmention;
	}

	public List<String> getBanned() {
		return banned;
	}

	public void setBanned(List<String> banned) {
		this.banned = banned;
	}

	public String getBfmentionleave() {
		return bfmentionleave;
	}

	public void setBfmentionleave(String bfmentionleave) {
		this.bfmentionleave = bfmentionleave;
	}

	public String getAfmentionleave() {
		return afmentionleave;
	}

	public void setAfmentionleave(String afmentionleave) {
		this.afmentionleave = afmentionleave;
	}

	public long getJoinId() {
		return JoinId;
	}

	public void setJoinId(long joinId) {
		JoinId = joinId;
	}

	public long getLogsId() {
		return logsId;
	}

	public void setLogsId(long logsId) {
		this.logsId = logsId;
	}

	public long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(long leaveId) {
		this.leaveId = leaveId;
	}

	public Role getAddcmd() {
		return Bot.getJda().getRoleById(addcmd);
	}

	public void setAddcmd(Role addcmd) {
		this.addcmd = addcmd.getIdLong();
	}

	public Role getMuted() {
		return Bot.getJda().getRoleById(muted);
	}

	public void setMuted(Role muted) {
		this.muted = muted.getIdLong();
	}

	public List<Warns> getWarns() {
		return warns;
	}

	public void setWarns(List<Warns> warns) {
		this.warns = warns;
	}
	 
}
