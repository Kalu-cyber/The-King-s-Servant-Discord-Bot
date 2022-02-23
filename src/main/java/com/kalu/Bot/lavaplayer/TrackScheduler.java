package com.kalu.Bot.lavaplayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter{
	public final AudioPlayer player;
	public final BlockingQueue<AudioTrack> queue;
	private boolean loop;
	public TrackScheduler(AudioPlayer player,boolean loop){
		this.player = player;
		queue = new LinkedBlockingQueue<>();
		this.loop = loop;
	}
	public void queue(AudioTrack track) {
		if(!this.player.startTrack(track, true)) {
			this.queue.offer(track);
		}
	}
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if(endReason.mayStartNext) {
			if(loop) {
				this.queue.offer(track.makeClone());
			}
			nextTrack();
		}else {
			if(loop) {
				this.queue.offer(track.makeClone());
				nextTrack();
			}
		}
		super.onTrackEnd(player, track, endReason);
	}

	public void nextTrack() {
		player.startTrack(this.queue.poll(),false);
		
	}
	public boolean isLoop() {
		return loop;
	}
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
}
