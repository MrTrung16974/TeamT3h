package com.nghianv.serverapi.controller;

import com.nghianv.serverapi.api.ActorApiController;
import com.nghianv.serverapi.dto.BaseResponse;
import com.nghianv.serverapi.model.ActorModel;
import com.nghianv.serverapi.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ActorController {
    @Autowired
    ActorRepository actorRepository;

    @Autowired
    ActorApiController actorApiController;

    @RequestMapping(value = "/admin/home",method = RequestMethod.GET)
    public String homeActor(Model model,@RequestParam(value = "name", defaultValue = "")String name,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "perPage", defaultValue = "5") int perPage) {
        BaseResponse response = new BaseResponse();
        response = actorApiController.Product(name, page, perPage);
        model.addAttribute("total", response.getTotal());
        model.addAttribute("listActor", response.getData());
        return "admin/index";
    }

    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public String detailActor(Model model, @PathVariable Integer id) {
        List<ActorModel> list = actorRepository.findAll();
        ActorModel actorModel = null;
        for (ActorModel a : list) {
            if(a.getId() == id) {
                actorModel = a;
            }
        }
        if(actorModel != null) {
            model.addAttribute("actor", actorModel);
        }else {
            System.out.println("NUll Data");
        }
        return "detail";
    }
}
