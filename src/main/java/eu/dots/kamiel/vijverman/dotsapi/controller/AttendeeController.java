/*
 */
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
 * @author kamie_itw2x3t
 */
@RestController
@RequestMapping("/Attendee")
public class AttendeeController {
    private String col = "Attendee";
    @RequestMapping("/getAll")
	public String getAll() {
            String res = TableDAO.getAllFromCollection(col);
            return res;
	}
        
        @RequestMapping(path="/getEmailById", method = RequestMethod.GET)
        public String getEmailById(@RequestParam(value = "id") String id) {
            return TableDAO.getStringValueFromDoc(col, id, "attendeeBarcode");
        }
        @RequestMapping("/getById")
        @ResponseBody
        public String getById(@RequestParam(value = "id") String id) {
            return TableDAO.getAllFromCollectionAndFilter(col, "_id", id);
        }
        @RequestMapping("/getSome")
        public String getSome(@RequestParam(value="page") String page,@RequestParam(value="size") String size) {
            Integer parsedPage = Integer.parseInt(page);
            Integer parsedSize = Integer.parseInt(size);
            return TableDAO.getSomeFromCol(col, parsedPage, parsedSize);
        }
        @RequestMapping("/count")
        public long count() {
            return TableDAO.count(col);
        }
        @RequestMapping(path = "/insert", method = RequestMethod.POST)
        @ResponseBody
        public String insertAttendee(@RequestBody String body) {
            return TableDAO.insertManyInCol(col, body);
        }
        @RequestMapping(path = "/update", method = RequestMethod.POST)
        @ResponseBody
        public Boolean updateAttendee(@RequestBody String body) {
            return TableDAO.updateManyInCol(col, body);
        }
        @RequestMapping(path = "/delete", method = RequestMethod.POST)
        @ResponseBody
        public Boolean deleteAttendee(@RequestBody String body) {
            return TableDAO.deleteManyInCol(col, body);
        }
    
}
