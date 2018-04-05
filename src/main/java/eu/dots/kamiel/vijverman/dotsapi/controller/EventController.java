package eu.dots.kamiel.vijverman.dotsapi.controller;

import eu.dots.kamiel.vijverman.dotsapi.dao.TableDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kamielV
 */
@RestController
@RequestMapping("/Event")
public class EventController {
    private String col = "Event";
    @RequestMapping("/getAll")
	public String getAll() {
		return TableDAO.getAllFromCollection(col);
	}
        @RequestMapping(path="/getSessions", method = RequestMethod.GET)
        public String getSessions(@RequestParam(value = "id") String id) {
            id = "Event$" + id;
            return TableDAO.getAllFromCollectionAndFilter("Session", "_p_sessionEvent", id);
        }
        @RequestMapping(path="/getAttendees", method = RequestMethod.GET)
        public String getAttendees(@RequestParam(value = "id") String id) {
            id = "Event$" + id;
            return TableDAO.getAllFromCollectionAndFilter("Attendee", "_p_attendeeEvent", id);
        }
        @RequestMapping(path="/getByName", method = RequestMethod.GET)
        public String getByName(@RequestParam(value = "id") String id) {
            id = ".*"+id+".*";
            return TableDAO.getByName(col, "eventName", id);
        }
        @RequestMapping(path = "/insert", method = RequestMethod.POST)
        @ResponseBody
        public String insertEvent(@RequestBody String body) {
            return TableDAO.insertManyInCol(col, body);
        }
        @RequestMapping(path = "/update", method = RequestMethod.POST)
        @ResponseBody
        public Boolean updateEvent(@RequestBody String body) {
            return TableDAO.updateManyInCol(col, body);
        }
        @RequestMapping(path = "/delete", method = RequestMethod.POST)
        @ResponseBody
        public Boolean deleteEvent(@RequestBody String body) {
            return TableDAO.deleteManyInCol(col, body);
        }
        @RequestMapping("getById")
        @ResponseBody
        public String getById(@RequestParam(value = "id") String id) {
            return TableDAO.getAllFromCollectionAndFilter(col, "_id", id);
        }
}
