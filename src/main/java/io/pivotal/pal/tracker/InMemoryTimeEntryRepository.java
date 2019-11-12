package io.pivotal.pal.tracker;

import java.sql.Time;
import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    TimeEntry timeEntry;
    Map<Long, TimeEntry> timeEntryMap = new HashMap();
    List<TimeEntry> timeEntryList = new ArrayList<>();
    int counter = 0;

    public TimeEntry create(TimeEntry any) {
        this.counter = counter+=1;
        any.setId(counter);
        this.timeEntryMap.put(any.getId(),any);
        return any;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return this.timeEntryMap.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        if (!timeEntryMap.isEmpty()) {
            for (Map.Entry<Long, TimeEntry> entry : timeEntryMap.entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                timeEntryList.add(entry.getValue());
            }
        }
        return this.timeEntryList;
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry any) {
        if(this.timeEntryMap.get(timeEntryId) != null) {
            any.setId(timeEntryId);
            this.timeEntryMap.put(any.getId(), any);
            return any;
        } else {
            return null;
        }
    }

    @Override
    public void delete(long timeEntryId) {
        this.timeEntryMap.remove(timeEntryId);
    }
}
