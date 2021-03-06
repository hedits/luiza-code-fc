package spg.finalchallenge.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spg.finalchallenge.entity.Client;
import spg.finalchallenge.payload.Response;
import spg.finalchallenge.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( "/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @ApiOperation(value = "save a new client")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Returns the registered client", response = Response.class),
            @ApiResponse(code = 401, message = "You do not have permission to access this feature.", response = Response.class),
            @ApiResponse(code = 400, message = "Bad request", response = Response.class),
            @ApiResponse(code = 500, message = "An exception was thrown", response = Response.class),
    })
    @PostMapping("/client")
    public ResponseEntity<?> createClient(@RequestBody Client client){
        try {
            Client clientResultCpf = clientService.findByCpf(client.getCpf());
            if (clientResultCpf == null){
                return new ResponseEntity<>(clientService.saveClient(client), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(new Response(false, "Cpf já existe!"), HttpStatus.CONFLICT);
        }catch (Exception e){
            return new ResponseEntity<>(new Response(false,"Request Errors"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Search client by Id")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Return Client", response = Response.class),
            @ApiResponse(code = 401, message = "You do not have permission to access this feature.", response = Response.class),
            @ApiResponse(code = 404, message = "List products not found", response = Response.class),
            @ApiResponse(code = 500, message = "An exception was thrown", response = Response.class),
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){
        try {
            Optional<Client> clientResultId = clientService.findById(id);
            if (clientResultId.isPresent()){
                return new ResponseEntity<>(clientResultId, HttpStatus.OK);
            }
            return new ResponseEntity<>(new Response(false, "Client not foud"),
                    HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(new Response(false, "Request Errors"),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(value = "Search all clients")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Returns the list of clients", response = Response.class),
            @ApiResponse(code = 401, message = "You do not have permission to access this feature.", response = Response.class),
            @ApiResponse(code = 404, message = "List products not found", response = Response.class),
            @ApiResponse(code = 500, message = "An exception was thrown", response = Response.class),
    })
    @GetMapping("/clients")
    public ResponseEntity<?> listAllClients(){
        try {
            List<Client> clients = clientService.listAllClient();
            if (clients != null){
                return new ResponseEntity<>(clients, HttpStatus.OK);
            }else{
                return new ResponseEntity(new Response(false, "Client not found"),
                        HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>( new Response(false, "Request Errors"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Update client")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Returns the client", response = Response.class),
            @ApiResponse(code = 401, message = "You do not have permission to access this feature.", response = Response.class),
            @ApiResponse(code = 404, message = "List products not found", response = Response.class),
            @ApiResponse(code = 500, message = "An exception was thrown", response = Response.class),
    })
    @PutMapping("/client/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable long id, @RequestBody Client client){
        try {
            Optional<Client> clientResult = clientService.findById(id);
            if (clientResult.isPresent()){
                client.setId(clientResult.get().getId());
                return new ResponseEntity<>(clientService.update(client), HttpStatus.OK);
            }else{
                return new ResponseEntity(new Response(false, "Client not found"),
                        HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>( new Response(false, "Request Errors"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Delete client by Id")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Returns the cpf deleted", response = Response.class),
            @ApiResponse(code = 401, message = "You do not have permission to access this feature.", response = Response.class),
            @ApiResponse(code = 404, message = "List products not found", response = Response.class),
            @ApiResponse(code = 500, message = "An exception was thrown", response = Response.class),
    })
    @DeleteMapping("client/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        try {
             Client clientResultId = clientService.getId(id);
            if (clientResultId != null){
                clientService.deleteClient(clientResultId);
                return new ResponseEntity<>(new Response(true, "Client deleted id: " + id ),
                        HttpStatus.OK);
            }
            return new ResponseEntity<>(new Response(false, "Id not found " + clientResultId.getId()), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(new Response(false, "Request erros"), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Delete client by Cpf")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Returns the cpf deleted", response = Response.class),
            @ApiResponse(code = 401, message = "You do not have permission to access this feature.", response = Response.class),
            @ApiResponse(code = 404, message = "List products not found", response = Response.class),
            @ApiResponse(code = 500, message = "An exception was thrown", response = Response.class),
    })
    @DeleteMapping("client/delete/{cpf}")
    public ResponseEntity<?> deleteClientByCpf(@PathVariable String cpf){
        try {
            Client client = clientService.findByCpf(cpf);
            if (client != null){
                clientService.deleteClient(client);
                return new ResponseEntity<>(new Response(true, "Cliente deleted cpd: " + cpf ),
                        HttpStatus.OK);
            }else {
                return new ResponseEntity(new Response(false, "Client not found"),
                        HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>( new Response(false, "Request Errors"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
