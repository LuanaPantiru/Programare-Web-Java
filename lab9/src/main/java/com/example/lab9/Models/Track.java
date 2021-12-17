package com.example.lab9.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @OneToOne
    @JoinColumn(name="speaker_id", referencedColumnName = "id")
    private Person speaker;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name ="attendee", joinColumns = @JoinColumn(name = "track_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> attendees;
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;
    @Column(name="date", nullable = false)
    private String date;
    @Column(name = "hour", nullable = false)
    private String hour;
    public  Track(){

    }

    public Track(String title, String description, Person speaker, String date, String hour) {
        this.title = title;
        this.description = description;
        this.speaker = speaker;
        this.date = date;
        this.hour = hour;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Person speaker) {
        this.speaker = speaker;
    }

    public Set<Person> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Person> attendees) {
        this.attendees = attendees;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    

    public void update(Track track){
        this.title = track.getTitle();
        this.description = track.getDescription();
        this.speaker = track.getSpeaker();
        this.date = track.getDate();
        this.hour = track.getHour();
    }
}
