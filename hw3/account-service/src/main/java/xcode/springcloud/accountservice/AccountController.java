package xcode.springcloud.accountservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value="/accounts", method= RequestMethod.POST)
    public ResponseEntity<Account> create(
            @RequestBody(required = false) Account account) {

        accountRepository.save(account);

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @RequestMapping(value="/accounts", method=RequestMethod.GET)
    public ResponseEntity<List<Account>> getAll(
            @RequestParam(value="firstName", defaultValue="") String firstName,
            @RequestParam(value="lastName", defaultValue="") String lastName) {

        List<Account> accounts = new ArrayList<>();

        if(firstName.equals("") && lastName.equals("")){
            accountRepository.findAll().iterator().forEachRemaining(accounts::add);
        }if(firstName.equals("")){
            accountRepository.findByLastName(lastName).iterator().forEachRemaining(accounts::add);
        }else if(lastName.equals("")){
            accountRepository.findByFirstName(firstName).iterator().forEachRemaining(accounts::add);
        }else{
            accountRepository.findByFirstNameAndLastName(firstName, lastName)
                    .iterator().forEachRemaining(accounts::add);
        }

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @RequestMapping(value="/accounts/{id}", method=RequestMethod.GET)
    public ResponseEntity<Account> get(@PathVariable("id") String id) {

        Account account = null;
        Optional<Account> optional = accountRepository.findById(Long.parseLong(id));

        if(!optional.isPresent()){
            return new ResponseEntity<>(account, HttpStatus.NOT_FOUND);
        }

        account = optional.get();

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}

