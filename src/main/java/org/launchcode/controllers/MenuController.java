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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("title", "menus");

        return "menu/index";
    }

    @GetMapping(value = "add")
    public String add(Model model) {
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
        return"redirect:view/" + menu.getId();
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
        AddMenuItemForm menuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());

        model.addAttribute("title","Add item to menu: " + menu.getName());
        model.addAttribute("form", menuItemForm);

        return "menu/add-item";
    }

    @PostMapping(value = "add-item/{menuId}")
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm menuItemForm, Errors errors,
                          @PathVariable int menuId){

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Menu Item");
            return "menu/add-item/" + menuId;
        }

        Menu menu = menuDao.findOne(menuItemForm.getMenuId());
        Cheese cheese = cheeseDao.findOne(menuItemForm.getCheeseId());

        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view/" + menu.getId();


    }



}
