package io.pivotal.pal.tracker;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = this.timeEntryRepository.create(timeEntryToCreate);
        if (timeEntry != null) {
            return new ResponseEntity(timeEntry, HttpStatus.CREATED);
        } else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry timeEntry = this.timeEntryRepository.find(timeEntryId);
        if(timeEntry != null) {
            return new ResponseEntity(timeEntry,  HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity(this.timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updatedTimeEntry = this.timeEntryRepository.update(timeEntryId, expected);
        if (updatedTimeEntry != null) {
            return new ResponseEntity(updatedTimeEntry, HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        this.timeEntryRepository.delete(timeEntryId);
        TimeEntry timeEntry = this.timeEntryRepository.find(timeEntryId);

        if (timeEntry == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity(HttpStatus.OK);
    }
}
