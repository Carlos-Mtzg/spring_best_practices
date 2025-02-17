package gregdev.spring_enterprises.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gregdev.spring_enterprises.model.EnterpriseModel;
import gregdev.spring_enterprises.service.EnterpriseService;
import gregdev.spring_enterprises.utils.Utilities;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;
    private static final String RECORD_NOT_FOUND = "Record not found";
    private static final String INTERNAL_SERVER_ERROR = "An internal server error ocurred.";
    private static final String SUCCESS_CODE = "AC200";
    private static final String NOTFOUND_CODE = "AC404";
    private static final String BADREQUEST_CODE = "AC400";
    private static final String INTERNAL_SERVER_ERROR_CODE = "AC500";

    EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    // Test
    @GetMapping("/test")
    public String test() {
        return "Ok";
    }

    // GetAll
    @GetMapping("/enterprise")
    public List<EnterpriseModel> getAll() {
        return this.enterpriseService.getAll();
    }

    // GetById
    @GetMapping("enterprise/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer id) {
        try {
            EnterpriseModel enterprise = this.enterpriseService.findById(id);
            if (enterprise != null) {
                return ResponseEntity.ok(enterprise);
            } else {
                return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
            }
        } catch (EntityNotFoundException e) {
            return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.BAD_GATEWAY, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // GetByUUID
    @GetMapping("enterprise/uuid/{uuid}")
    public ResponseEntity<Object> getByUuid(@PathVariable("uuid") String uuidStr) {
        try {
            UUID uuid = parseUuid(uuidStr);
            EnterpriseModel enterprise = this.enterpriseService.findByUuid(uuid);
            if (enterprise != null) {
                return ResponseEntity.ok(enterprise);
            } else {
                return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
            }
        } catch (IllegalArgumentException e) {
            return Utilities.generateResponse(HttpStatus.BAD_REQUEST, "Invalid UUID format", BADREQUEST_CODE);
        } catch (EntityNotFoundException e) {
            return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.BAD_GATEWAY, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // Create
    @PostMapping("/enterprise")
    public ResponseEntity<Object> createEnterprise(@RequestBody EnterpriseModel request) {
        try {
            String razonSocial = request.getRazonSocial();
            String rfc = request.getRfc();
            String telefono = request.getTelefono();
            String contacto = request.getContacto();
            String correo = request.getCorreo();

            if (razonSocial != null && rfc != null && telefono != null && contacto != null && correo != null) {
                this.enterpriseService.save(request);
                return Utilities.generateResponse(HttpStatus.OK, "Record created successfully", SUCCESS_CODE);
            }
            return Utilities.generateResponse(HttpStatus.BAD_REQUEST, "All fields are required.", BADREQUEST_CODE);
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // Delete
    @DeleteMapping("/enterprise/{id}")
    public ResponseEntity<Object> deleteEnterprise(@PathVariable("id") Integer id) {
        try {
            EnterpriseModel enterprise = this.enterpriseService.findById(id);
            if (enterprise == null) {
                return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
            } else {
                this.enterpriseService.delete(id);
                return Utilities.generateResponse(HttpStatus.OK, "Record deleted successfully", SUCCESS_CODE);
            }
        } catch (EntityNotFoundException e) {
            return Utilities.generateResponse(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND, NOTFOUND_CODE);
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR_CODE);
        }
    }

    // Método para validar el UUID
    private UUID parseUuid(String uuidStr) {
        return UUID.fromString(uuidStr);
    }
}
