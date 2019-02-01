package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @GetMapping(value = "add")
    public String displayAddMenuform(Model model) {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @PostMapping(value = "add")
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {
        if (errors.hasErrors()){
            model.addAttribute("title", "add menu");
            return "menu/add";

        }

        menuDao.save(menu);
        return"redirect:" + menu.getId();
    }

    @GetMapping(value = "view/{id}")
    public String viewMenu(Model model, @PathVariable int id){
        Menu menu = menuDao.findOne(id);
        model.addAttribute("title", menu.getName());
        model.addAttribute("menu", menu);
        return "menu/view";
    }

    @GetMapping(value = "add-item/{menuId}")
    public String addItem (Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm AddMenuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());

        model.addAttribute("title","Add item to menu:" + menu.getName());
        model.addAttribute("form", AddMenuItemForm);

        return "menu/add-item";
    }

    @PostMapping(value = "/menu/add-item/")
    public String addItem (Model model, @Valid AddMenuItemForm AddMenuItemForm, Errors errors, @PathVariable int MenuId){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add Menu Item");
            return "menu/add-item";
        }
        Menu menu = menuDao.findOne(AddMenuItemForm.getMenuId());
        Cheese cheese = cheeseDao.findOne(AddMenuItemForm.getCheeseId());

        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:menu/add-item/view" + menu.getId();


    }



}
