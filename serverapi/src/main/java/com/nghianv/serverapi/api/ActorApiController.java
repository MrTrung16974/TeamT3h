package com.nghianv.serverapi.api;

import com.nghianv.serverapi.dto.ActorRequest;
import com.nghianv.serverapi.dto.BaseResponse;
import com.nghianv.serverapi.model.ActorModel;
import com.nghianv.serverapi.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/v1/api")
public class ActorApiController {
    @Autowired
    private ActorRepository actorRepository;

    //Lấy tất cả actors trong DB
    @RequestMapping(value = "/actors",method = RequestMethod.GET)
    //@GetMapping("/actors")
    public BaseResponse getAllActor(){
        BaseResponse response = new BaseResponse();
        response.setCode("00");
        response.setMessage("Success");
        response.setData(actorRepository.findAll());
        return response;
    }

    @RequestMapping("/actor/{id}")
    public BaseResponse getActor(@PathVariable("id") int index){
        BaseResponse response = new BaseResponse();
        Optional<ActorModel> optActor = actorRepository.findById(index);
        if(optActor.isPresent()){
            response.setCode("00");
            response.setMessage("Success");
            response.setData(optActor.get());
        }else{
            response.setCode("99");
            response.setMessage("Not found");
            response.setData(null);
        }
        return response;
    }

    @RequestMapping(value = "/actor",method = RequestMethod.POST)
    public BaseResponse createActor(@RequestBody ActorRequest actorRequest){
        BaseResponse response = new BaseResponse();
        if(actorRequest.getName().isEmpty() ||
                actorRequest.getCountry().isEmpty()){
            response.setCode("98");
            response.setMessage("Data invalid");
            response.setData(null);
            return response;
        }
        ActorModel newActor = new ActorModel();
        newActor.setName(actorRequest.getName());
        newActor.setAge(actorRequest.getAge());
        newActor.setCountry(actorRequest.getCountry());
        response.setCode("00");
        response.setMessage("Create new Actor imformation success");
        response.setData(actorRepository.save(newActor));
        return response;
    }

    @RequestMapping(value = "/actor/{id}",method = RequestMethod.PUT)
    public BaseResponse updateActor(@PathVariable("id") int id,
                                      @RequestBody ActorRequest actorRequest){
        BaseResponse response = new BaseResponse();
        Optional<ActorModel> optionalActorModel = actorRepository.findById(id);
        if(optionalActorModel.isPresent()){
            ActorModel exitsActor = optionalActorModel.get();
            if(!actorRequest.getName().isEmpty()){
                exitsActor.setName(actorRequest.getName());
            }
            if(!actorRequest.getCountry().isEmpty()){
                exitsActor.setCountry(actorRequest.getCountry());
            }

            response.setCode("00");
            response.setMessage("Update Actor imformation success");
            response.setData(actorRepository.save(exitsActor));
        }else{
            response.setCode("99");
            response.setMessage("Data Not found");
            response.setData(null);
        }
        return response;

    }

    @DeleteMapping("/actor/{id}")
    public BaseResponse deleteActor(@PathVariable("id")int id){
        BaseResponse response = new BaseResponse();
        try{
            Optional<ActorModel> optActor = actorRepository.findById(id);
            //throw new Exception("DB die");
            if(optActor.isPresent()){
                actorRepository.deleteById(id);
                response.setCode("00");
                response.setMessage("Delete actor success");
                response.setData(id);
            }else{
                response.setCode("99");
                response.setMessage("Data not found");
                response.setData(id);
            }
        }catch (Exception e){
            response.setCode("90");
            response.setMessage("System erorr : " + e.getMessage());
            response.setData(id);
        }
        return response;
    }
    @RequestMapping(value = "/actor/search", method = RequestMethod.GET)
    public BaseResponse Product(@RequestParam(value = "name", required = false)String name,
                                @RequestParam("page") int page,
                                @RequestParam("perPage") int perPage) {
        BaseResponse response = new BaseResponse();
        try {
            Pageable pageable = PageRequest.of(page, perPage, Sort.by(Sort.Direction.ASC,"id"));
            Page<ActorModel> listActor = actorRepository.findByNameContaining(name.toLowerCase(),pageable);
            Integer totalPage = listActor.getTotalPages();
            if (!listActor.isEmpty()) {
                response.setCode("00");
                response.setMessage("List actor search by key: " + name);
                response.setTotal(totalPage);
                response.setData(listActor);
            } else {
                response.setCode("99");
                response.setMessage("Data not found");
                response.setTotal(null);
                response.setData(null);
            }
        } catch (Exception e) {
            response.setCode("90");
            response.setMessage("System erorr : " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
}
