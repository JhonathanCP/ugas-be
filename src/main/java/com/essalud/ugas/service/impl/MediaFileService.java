package com.essalud.ugas.service.impl;

import java.util.Optional;

import com.essalud.ugas.model.MediaFile;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IMediaFileRepo;
import com.essalud.ugas.service.IMediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaFileService extends CRUDImpl<MediaFile, Integer> implements IMediaFileService{

    @Autowired
    private IMediaFileRepo repo;

    @Override
    protected IGenericRepo<MediaFile, Integer> getRepo() {
        return repo;
    }

    @Override
	public int saveFile(MediaFile mediafile) {
		MediaFile ar = repo.save(mediafile);
		return ar.getIdMediaFile() > 0 ? 1 : 0;
	}

	@Override
	public byte[] getMediaFile(Integer idMediaFile) {		
		Optional<MediaFile> op = repo.findById(idMediaFile);		
		return op.isPresent() ? op.get().getFile() : new byte[0];
	}

}
