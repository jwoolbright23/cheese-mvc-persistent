package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.data.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("category")
@Controller
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title" , "Categories");

        return "category/index";

    }

    @GetMapping(value = "add")
    public String displayAddCategoryform(Model model){
        model.addAttribute(new Category());
        model.addAttribute("title", "Add Category");

        return "category/add";

    }

    @PostMapping(value = "add")
    public String add(Model model, @ModelAttribute @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("title" , "add category");
            return "category/add";
        }

        categoryDao.save(category);
        return "redirect:";
    }

}
