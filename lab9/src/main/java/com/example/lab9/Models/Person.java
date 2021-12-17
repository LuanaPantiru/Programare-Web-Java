package com.example.lab9.Models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name ="first_name",nullable = false)
    private String firstName;
    @Column(name ="last_name",nullable = false)
    private String lastName;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name ="attendee", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "track_id"))
    private Set<Track> tracks;

    public Person(){

    }
    public Person(String firstName,String lastName){
        this.lastName = lastName;
        this.firstName = firstName;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(Track track){
        tracks.add(track);
    }

    public void removeTrack(Track track){
        tracks.remove(track);
    }

    public void update(Person person){
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }
}
