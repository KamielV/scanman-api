/*
 */
package eu.dots.kamiel.vijverman.dotsapi.controller;
import eu.dots.kamiel.vijverman.dotsapi.dao.TableDAO;
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
@RequestMapping("/Session")
public class SessionController {
    private String col = "Session";
    @RequestMapping("/getAll")
	public String getAll() {
		// Aanroepen met
		// http://localhost:8080/getAll
		return TableDAO.getAllFromCollection(col);
	}
        @RequestMapping(path="/getAttendees", method = RequestMethod.GET)
        public String getAttendees(@RequestParam(value = "id") String id) {
            id = "Session$" + id;
            return TableDAO.getAttendeesFromSession("SessionAttendee", "_p_session", id);
        }
        @RequestMapping("getById")
        @ResponseBody
        public String getById(@RequestParam(value = "id") String id) {
            return TableDAO.getAllFromCollectionAndFilter(col, "_id", id);
        }
        @RequestMapping(path = "/insert", method = RequestMethod.POST)
        @ResponseBody
        public String insertSession(@RequestBody String body) {
            return TableDAO.insertManyInCol(col, body);
        }
        @RequestMapping(path = "/update", method = RequestMethod.POST)
        @ResponseBody
        public Boolean updateSession(@RequestBody String body) {
            return TableDAO.updateManyInCol(col, body);
        }
        @RequestMapping(path = "/delete", method = RequestMethod.POST)
        @ResponseBody
        public Boolean deleteSession(@RequestBody String body) {
            return TableDAO.deleteManyInCol(col, body);
        }
}
