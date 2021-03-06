package com.buckwa.service.intf.admin;

import com.buckwa.domain.common.BuckWaRequest;
import com.buckwa.domain.common.BuckWaResponse;

public interface LovHeaderService {
	
	public BuckWaResponse create(BuckWaRequest request); 
	public BuckWaResponse getAll();	 
	public BuckWaResponse update(BuckWaRequest request);
	public BuckWaResponse getByOffset(BuckWaRequest request);	
	public BuckWaResponse getById(BuckWaRequest request);
	public BuckWaResponse deleteById(BuckWaRequest request);
	
	
	public BuckWaResponse createLOVDetail(BuckWaRequest request); 
	
	public BuckWaResponse deleteDetailById(BuckWaRequest request);
	public BuckWaResponse getDetailById(BuckWaRequest request);
	public BuckWaResponse updateDetail(BuckWaRequest request);
	public BuckWaResponse getDetailsByCode(BuckWaRequest request);
}
