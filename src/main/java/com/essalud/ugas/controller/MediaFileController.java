package com.essalud.ugas.controller;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.essalud.ugas.service.IFileTypeService;
import com.essalud.ugas.service.IMediaFileService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.essalud.ugas.dto.MediaFileDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.MediaFile;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
@RequestMapping("/media-file")
public class MediaFileController {

	@Autowired
    private ModelMapper mapper;

    @Autowired
    private IMediaFileService mediaFileService;

	@Autowired
    private IFileTypeService fileTypeService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Integer> guardarMediaFile(@RequestParam("adjunto") MultipartFile file, @RequestParam("idFileType") Integer idFileType) throws Exception{

		int rpta = 0;

		MediaFile ar = new MediaFile();
		ar.setFileType(fileTypeService.findById(idFileType));
		ar.setFileExtension(file.getContentType());
		ar.setName(file.getOriginalFilename());
		ar.setFile(file.getBytes());
        ar.setActive(true);
		
		rpta = mediaFileService.saveFile(ar);
		
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	@GetMapping("/{idMediaFile}")
    public ResponseEntity<MediaFileDTO> findById(@PathVariable Integer idMediaFile) {
        MediaFileDTO dtoResponse;
        MediaFile obj = mediaFileService.findById(idMediaFile);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + idMediaFile);
        } else {
            dtoResponse = mapper.map(obj, MediaFileDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

	@GetMapping(value = "/{idMediaFile}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> getMediaFile(@PathVariable("idMediaFile") Integer idMediaFile) throws IOException {

		byte[] arr = mediaFileService.getMediaFile(idMediaFile);

		return new ResponseEntity<>(arr, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        MediaFile obj = mediaFileService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            mediaFileService.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}