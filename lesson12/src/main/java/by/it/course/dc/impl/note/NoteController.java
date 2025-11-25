package by.it.course.dc.impl.note;

import by.it.course.dc.impl.note.model.NoteRequestTo;
import by.it.course.dc.impl.note.model.NoteResponseTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1.0/notes")
@Data
@AllArgsConstructor
@Valid
@Tag(name = "Notes API", description = "Management of notes related to stories.")
public class NoteController {

    private NoteService noteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @AllowFiltering
    @Operation(
            summary = "Retrieve all notes",
            description = "Fetches a list of all existing notes.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = NoteResponseTo.class))
                            )
                    )
            }
    )
    public List<NoteResponseTo> findAll() {
        return noteService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get a note by ID",
            description = "Retrieves a specific note based on its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved note",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NoteResponseTo.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Note not found"
                    )
            }
    )
    public NoteResponseTo findOne(@PathVariable("id") long id) {
        return noteService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new note",
            description = "Creates a new note and returns the created object.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NoteRequestTo.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Note successfully created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NoteResponseTo.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or validation failed"
                    )
            }
    )
    public NoteResponseTo create(@RequestBody NoteRequestTo dto, HttpServletRequest request, Locale locale) {
        return noteService.create(dto, locale);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update an existing note",
            description = "Updates the content of an existing note. Note ID must be provided in the request body.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NoteRequestTo.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Note successfully updated",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NoteResponseTo.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Note not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or validation failed"
                    )
            }
    )
    public NoteResponseTo update(@Valid @RequestBody NoteRequestTo dto, HttpServletRequest request) {
        return noteService.update(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a note by ID",
            description = "Deletes a specific note based on its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Note successfully deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Note not found"
                    )
            }
    )
    public void delete(@Valid @PathVariable("id") Long id) {
        noteService.removeById(id);
    }
}