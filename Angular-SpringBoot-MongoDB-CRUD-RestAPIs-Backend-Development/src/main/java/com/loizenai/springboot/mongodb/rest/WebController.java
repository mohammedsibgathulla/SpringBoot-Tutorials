package com.loizenai.springboot.mongodb.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loizenai.springboot.mongodb.exception.CustomException;
import com.loizenai.springboot.mongodb.message.ResponseMsg;
import com.loizenai.springboot.mongodb.model.Customer;
import com.loizenai.springboot.mongodb.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = { "http://localhost:4200"})
public class WebController {
	
	@Autowired
	CustomerService customerService;

	@PostMapping("/create")
	public ResponseEntity<ResponseMsg> saveCustomer(@RequestBody Customer customer, HttpServletRequest request) {
		try {
			// save to MongoDB database
			Customer _customer = customerService.saveCustomer(customer);
			
			String message = "Upload Successfully a Customer to MongoDB with id = " + _customer.getId();
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(message, request.getRequestURI(), 
											List.of(customer)), HttpStatus.OK);	
		}catch(Exception e) {
			String message = "Can NOT upload  a Customer to MongoDB database";
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(message, request.getRequestURI(), 
											e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("/retrieveinfos")
	public ResponseEntity<ResponseMsg> getAllCustomers(HttpServletRequest request) {
		try {
			// get all documents from MongoDB database
			List<Customer> customers = customerService.retrieveAllCustomers();
			
			String message = "Retrieve all Customer successfully!";
			
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(message, 
									request.getRequestURI(), customers), HttpStatus.OK);	
		}catch(Exception e) {
			String message = "Can NOT retrieve all data from MongoDB database";
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(message, request.getRequestURI(), 
									e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updatebyid/{id}")
	public ResponseEntity<ResponseMsg> updateCustomer(@PathVariable String id, @RequestBody Customer customer,
															HttpServletRequest request) {
		try {
			// update a customer to MongoDB
			Customer _customer = customerService.updateCustomer(id, customer);
			String message = "Successfully Update a Customer to MongoDB with id = " + id;
			
			return new ResponseEntity<ResponseMsg> (new ResponseMsg(message, request.getRequestURI(), 
														List.of(_customer)), HttpStatus.OK);
		} catch (CustomException ce) {
			String message = "Can NOT update to MongoDB a Customer with id = " + id;
			return new ResponseEntity<ResponseMsg> (new ResponseMsg(message, request.getRequestURI(), 
					ce.getMessage()), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			String message = "Can NOT update to MongoDB a Customer with id = " + id;
			return new ResponseEntity<ResponseMsg> (new ResponseMsg(message, request.getRequestURI(), 
					e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<ResponseMsg> deleteCustomerById(@PathVariable String id, HttpServletRequest request) {
		try {
			// delete a Customer from MongoDB database using ID
			customerService.deleteCustomerById(id);
			
			String message = "Successfully delete a Customer from MongoDB database with id = " + id;
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(message, request.getRequestURI()), HttpStatus.OK);
		} catch(Exception e) {
			String message = "Can Not delete a Customer from MongoDB database with id = " + id;
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(message, request.getRequestURI(), e.getMessage()), 
														HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}