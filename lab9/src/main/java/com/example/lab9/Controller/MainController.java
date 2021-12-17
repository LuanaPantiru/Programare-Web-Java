package com.example.lab9.Controller;

import com.example.lab9.Models.Person;
import com.example.lab9.Models.Room;
import com.example.lab9.Models.Track;
import com.example.lab9.Repositories.PersonRepository;
import com.example.lab9.Repositories.RoomRepository;
import com.example.lab9.Repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping(path="/persons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Person>> getPersons(){
        Collection<Person> persons = (Collection<Person>) personRepository.findAll();
        if(!persons.isEmpty()){
            return ResponseEntity.ok(persons);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/persons/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPerson(@PathVariable("id") long id){
        Optional<Person> person = personRepository.findById(id);
        if(person.isPresent()){
            return ResponseEntity.ok(person.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/persons", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addPerson(String firstName, String lastName){
        Person person = new Person(firstName, lastName);
        personRepository.save(person);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("persons").slash(person.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path="/persons/{id}")
    public ResponseEntity<Void> changePerson(@PathVariable("id") long id, @RequestBody Person entity){
        Optional<Person> existingPerson = personRepository.findById(id);
        if(existingPerson.isPresent()){
            existingPerson.get().update(entity);
            personRepository.save(existingPerson.get());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path="/persons/{id}")
    public ResponseEntity<Void> removePerson(@PathVariable("id") long id){
        Optional<Person> existingPerson = personRepository.findById(id);
        if(existingPerson.isPresent()){
            personRepository.delete(existingPerson.get());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/persons/{personId}/tracks")
    public ResponseEntity<Set<Track>> getPersonTracks(@PathVariable("personId") long personId){
        Optional<Person> person = personRepository.findById(personId);
        if(person.isPresent()){
            Set<Track> tracks = person.get().getTracks();
            if(!tracks.isEmpty()){
                return ResponseEntity.ok(tracks);
            }else{
                return ResponseEntity.noContent().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path="/persons/{personId}/tracks/{trackId}")
    public ResponseEntity<List<String>> addPersonTrack(@PathVariable("personId") long personId, @PathVariable("trackId") long trackId){
        Optional<Person> person = personRepository.findById(personId);
        if(person.isPresent()){
            Optional<Track> track = trackRepository.findById(trackId);
            if(track.isPresent() && track.get().getSpeaker().getId() != personId){
                person.get().addTrack(track.get());
                personRepository.save(person.get());
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.badRequest().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path="/persons/{personId}/tracks/{trackId}")
    public ResponseEntity<List<String>> removePersonTrack(@PathVariable("personId") long personId, @PathVariable("trackId") long trackId){
        Optional<Person> person = personRepository.findById(personId);
        if(person.isPresent()){
            Optional<Track> track = trackRepository.findById(trackId);
            if(track.isPresent()){
                person.get().removeTrack(track.get());
                personRepository.save(person.get());
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.badRequest().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/tracks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Track>> getTracks(){
        Collection<Track> tracks = (Collection<Track>) trackRepository.findAll();
        if(!tracks.isEmpty()){
            return ResponseEntity.ok(tracks);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/tracks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> getTrack(@PathVariable("id") long id){
        Optional<Track> track = trackRepository.findById(id);
        if(track.isPresent()){
            return ResponseEntity.ok(track.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path ="/tracks", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addTrack(String title, String description, long speakerId, String date, String hour){
        Optional<Person> speaker = personRepository.findById(speakerId);
        if(speaker.isPresent()){
            Track track = new Track(title,description,speaker.get(), date, hour);
            trackRepository.save(track);
            URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("tracks").slash(track.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path="/tracks/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeTrack(@PathVariable("id") long id, @RequestBody Track track){
        Optional<Track> existingTrack = trackRepository.findById(id);
        if(existingTrack.isPresent()){
            existingTrack.get().update(track);
            trackRepository.save(existingTrack.get());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path="/tracks/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable("id") long id, @RequestBody Track track){
        Optional<Track> existingTrack = trackRepository.findById(id);
        if(existingTrack.isPresent()){
            existingTrack.get().update(track);
            trackRepository.delete(existingTrack.get());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/tracks/{id}/attendees")
    public ResponseEntity<Set<Person>> getTrackPersons(@PathVariable("id") long id){
        Optional<Track> existingTrack = trackRepository.findById(id);
        if(existingTrack.isPresent()){
            Set<Person> persons = existingTrack.get().getAttendees();
            if(!persons.isEmpty()){
                return ResponseEntity.ok(persons);
            }else{
                return ResponseEntity.noContent().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/rooms", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Iterable<Room>> getRooms(){
        Collection<Room> rooms = (Collection<Room>) roomRepository.findAll();
        if(!rooms.isEmpty()){
            return ResponseEntity.ok(rooms);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/rooms/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> getRoom(@PathVariable("id") long id){
        Optional<Room> room = roomRepository.findById(id);
        if(room.isPresent()){
            return ResponseEntity.ok(room.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path ="/rooms", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addRoom(String name){
        Room room = new Room(name);
        roomRepository.save(room);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("rooms").slash(room.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path="/rooms/{id}")
    public ResponseEntity<Void> changeRoom(@PathVariable("id") long id, @RequestBody Room entity){
        Optional<Room> existingRoom = roomRepository.findById(id);
        if(existingRoom.isPresent()){
            existingRoom.get().setName(entity.getName());
            roomRepository.save(existingRoom.get());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path="/rooms/{id}")
    public ResponseEntity<Void> removeRoom(@PathVariable("id") long id){
        Optional<Room> existingRoom = roomRepository.findById(id);
        if(existingRoom.isPresent()){
            roomRepository.delete(existingRoom.get());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/rooms/{roomId}/tracks/{trackId}")
    public ResponseEntity<Void> addTrackToRoom(@PathVariable("roomId") long roomId, @PathVariable("trackId") long trackId){
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()){
            Optional<Track> track = trackRepository.findById(trackId);
            if(track.isPresent() && track.get().getRoom() == null && !verifyDateAndHour(room.get(),track.get().getDate(), track.get().getHour()) ){
                track.get().setRoom(room.get());
                trackRepository.save(track.get());
                room.get().addTrack(track.get());
                roomRepository.save(room.get());
                return ResponseEntity.ok().build();
            }
            else{
                return ResponseEntity.badRequest().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(path = "/rooms/{roomId}/tracks")
    public ResponseEntity<Set<Track>> getRoomTracks(@PathVariable("roomId") long roomId){
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()){
            Collection<Track> tracks = (Collection<Track>) trackRepository.findAll();
            Set<Track> roomTracks = new HashSet<>();
            for(Track track : tracks){
                if(track.getRoom() != null && track.getRoom().getId() == roomId)
                    roomTracks.add(track);
            }
            if(!tracks.isEmpty()){
                return ResponseEntity.ok(roomTracks);
            }else{
                return ResponseEntity.noContent().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    private boolean verifyDateAndHour(Room room, String date, String hour){
        Set<Track> tracks = room.getTracks();
        for(Track track : tracks){
            if(track.getDate().compareTo(date) == 0 && track.getHour().compareTo(hour) == 0){
                return true;
            }
        }
        return false;
    }


}
