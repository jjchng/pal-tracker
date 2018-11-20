package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;

        timeEntrySummary = meterRegistry.summary("Summary");
        actionCounter = meterRegistry.counter("Counter");

    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        actionCounter.increment();
        TimeEntry timeEntry = this.timeEntryRepository.create(timeEntryToCreate);
        if (timeEntry != null) {
            timeEntrySummary.record(timeEntryRepository.list().size());
            return new ResponseEntity(timeEntry, HttpStatus.CREATED);
        } else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        actionCounter.increment();
        TimeEntry timeEntry = this.timeEntryRepository.find(timeEntryId);
        if(timeEntry != null) {
            return new ResponseEntity(timeEntry,  HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity(this.timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        actionCounter.increment();
        TimeEntry updatedTimeEntry = this.timeEntryRepository.update(timeEntryId, expected);
        if (updatedTimeEntry != null) {
            return new ResponseEntity(updatedTimeEntry, HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        actionCounter.increment();
        this.timeEntryRepository.delete(timeEntryId);
        timeEntrySummary.record(timeEntryRepository.list().size());

        TimeEntry timeEntry = this.timeEntryRepository.find(timeEntryId);

        if (timeEntry == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity(HttpStatus.OK);
    }
}
