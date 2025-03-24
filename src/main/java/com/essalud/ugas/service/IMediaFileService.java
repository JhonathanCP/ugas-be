package com.essalud.ugas.service;

import com.essalud.ugas.model.MediaFile;

public interface IMediaFileService extends ICRUD<MediaFile, Integer>{

    int saveFile(MediaFile mediaFile);
	byte[] getMediaFile(Integer idMediaFile);

}
