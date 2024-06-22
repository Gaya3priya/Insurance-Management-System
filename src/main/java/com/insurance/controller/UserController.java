package com.insurance.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.insurance.dao.PolicyRepository;
import com.insurance.dao.UserRepository;
import com.insurance.entities.Policy;
import com.insurance.entities.User;
import com.insurance.helper.Message;
import com.insurance.service.PoliciesPDFExporter;
import com.insurance.service.UserService;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/user")
public class UserController {
 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PolicyRepository policyRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService service;
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String userName=principal.getName();
		System.out.println("USRNAME "+userName);
		//get the user using  username(Email)
		User user=userRepository.getUserByUserName(userName);
		System.out.println("USER "+user);
		
		model.addAttribute("user", user);
	}
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal ) {
		
		model.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
	}
	//open add form handler
	@GetMapping("/add-policy")
	public String openAddPolicyForm(Model model) {
		
		model.addAttribute("title","Add Policy");
		model.addAttribute("policy",new Policy());
		
		return "normal/add_policy_form";
	}
	//processing add policy form
	
	@PostMapping("/process-policy")
	public String processPolicy(@ModelAttribute Policy policy,Principal principal,HttpSession session) {
		
		try {
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);
		policy.setUser(user);
		user.getPolicies().add(policy);
		this.userRepository.save(user);
		System.out.println("DATA"+policy);
		System.out.println("Added data to database");
		
		//message success
		session.setAttribute("message", new Message("Your policy is added!! Add more...","success"));
		}catch(Exception e) {
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
			//error message
			session.setAttribute("message", new Message("Something went wrong!! Try again","danger"));
			
		}
		return "normal/add_policy_form";
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy",Locale.US),true));
		//binder.registerCustomEditor(getClass(), null, null);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	//show policies handler
	//per page=5[n]-no.of policies per page
	//current page=0[page]
	@GetMapping("/show-policies/{page}")
	public String showPolicies(@PathVariable("page") Integer page ,Model m,Principal principal) {
		m.addAttribute("title","View Policies");
		// retrieve policies list
		 String userName=principal.getName();
		 User user=this.userRepository.getUserByUserName(userName);
		 //current page-page
		 //policy per page-5
		 Pageable pageable = PageRequest.of(page,5);
		 
		 Page<Policy> policies=this.policyRepository.findPoliciesByUser(user.getId(),pageable);
		 
		 m.addAttribute("policies",policies);
		 m.addAttribute("currentPage",page);
		 
		 m.addAttribute("totalPages",policies.getTotalPages());
		  
		
		return "normal/show_policies";
	}
	//showing particular policy detail
	@RequestMapping("/{pId}/policy")
	public String showPolicyDetail(@PathVariable("pId") Integer pId,Model model,Principal principal) {
		System.out.println("PID"+pId);
		
		Optional<Policy> policyOptional =this.policyRepository.findById(pId);
		Policy policy= policyOptional.get();
		//security
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		if(user.getId()==policy.getUser().getId()) {
			model.addAttribute("policy", policy);
		}
		return "normal/policy_detail";
	}
	//delete policy handler
	@GetMapping("/delete/{pid}")
	public String deletePolicy(@PathVariable("pid")Integer pId,Model model,HttpSession session,Principal principal) {
        System.out.println("PID"+pId);
		
		Policy policy=this.policyRepository.findById(pId).get();
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		user.getPolicies().remove(policy);
		this.userRepository.save(user);
		System.out.println("DELETED");
		session.setAttribute("message", new Message("Policy deleted successfully...","success"));
		
		return "redirect:/user/show-policies/0";
	}
	//open update policy form handler
	@PostMapping("/update-policy/{pid}")
	public String updatePolicy(@PathVariable("pid")Integer pId,Model m) {
		
		m.addAttribute("title", "Update Policy");
		Policy policy=this.policyRepository.findById(pId).get();
		m.addAttribute("policy", policy);
		return "normal/update_form";
	}
	//processing update form at backend
	@RequestMapping(value="/process-update",method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Policy policy,Model m,HttpSession session,Principal principal) {
		//httpsession is for message purpose
		User user=this.userRepository.getUserByUserName(principal.getName());
		policy.setUser(user);
		
		this.policyRepository.save(policy);
		session.setAttribute("message", new Message("Policy updated successfully...","success"));
		
		System.out.println("Policy name="+policy.getName());
		System.out.println("Policy pid="+policy.getpId());
		return "redirect:/user/"+policy.getpId()+"/policy";
	}
	//your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		return "normal/profile";
	}
	//open settings handler
	@GetMapping("/settings")
	public String openSettings() {
		return "normal/settings";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session) {
		System.out.println("OLD PASSWORD:"+oldPassword);
		System.out.println("NEW PASSWORD:"+newPassword);
		//request param is for geting data from form
		String userName=principal.getName();
		User currentUser=this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			//change the password
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your password is successfully changed...","success"));
		}else {
			//error
			session.setAttribute("message", new Message(" Your old password is wrong.Please enter correct one...","danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
	@GetMapping("/export")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime =dateFormatter.format(new Date());
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename=policyholders_"+currentDateTime+".pdf";
		
		response.setHeader(headerKey, headerValue);
		List <Policy> policyHoldersList= service.listAll();
		
		PoliciesPDFExporter exporter=new PoliciesPDFExporter(policyHoldersList);
		
		exporter.export(response);
	}
}
