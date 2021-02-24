package com.sdc2ch.web.admin.repo.init;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.ISetupData;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.web.admin.repo.dao.T_BconMappingRepository;
import com.sdc2ch.web.admin.repo.domain.op.T_BCON_MAPPING;

@Component
public class AdminBeaconMappingData implements ISetupData {

	@Autowired T_BconMappingRepository bconMapRepo;
	
	@Override
	public void install() {
		
		









		




		
		
		List<T_BCON_MAPPING> bcons = new ArrayList<>();
		
		AtomicLong inc = new AtomicLong();
		
		T_BCON_MAPPING mapping = new T_BCON_MAPPING();
		mapping.setBconId("f37b728828c01d6318eb78dca2c1a02b");
		mapping.setBconName("SDC2CH-01");
		mapping.setFctryCd("4D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.OFFICE);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("b731cfc58de1c5cc5ad174a8f0e98414");
		mapping.setBconName("SDC2CH-02");
		mapping.setFctryCd("4D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.CU);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("3d212a3d58ab0a557fcff168db2cfb31");
		mapping.setBconName("SDC2CH-03");
		mapping.setFctryCd("4D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FLAT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("43343d6d30e681cf09b9ee5f9eaf092b");
		mapping.setBconName("SDC2CH-04");
		mapping.setFctryCd("4D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.PRCSSGD);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("54b9b9667306beec0c22c9b54cb59428");
		mapping.setBconName("SDC2CH-05");
		mapping.setFctryCd("4D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("33e9971ae973847157b7c3bdd693e01b");
		mapping.setBconName("SDC2CH-06");
		mapping.setFctryCd("4D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("d045ef9fe143eb425923e5ad5120c73c");
		mapping.setBconName("SDC2CH-09");
		mapping.setFctryCd("3D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("eb92bcc7d8b8c771c10453ea0c59c60e");
		mapping.setBconName("SDC2CH-10");
		mapping.setFctryCd("3D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("b6e6a3925ddc4e0cc89f7c9cffd07812");
		mapping.setBconName("SDC2CH-11");
		mapping.setFctryCd("2D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();
		mapping.setBconId("06f6c5bb5e6f00ba835b537d1ae8d22c");
		mapping.setBconName("SDC2CH-12");
		mapping.setFctryCd("2D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		
		mapping = new T_BCON_MAPPING();	
		mapping.setBconId("9069e40f933470a3f1b18bb2f26f3021");
		mapping.setBconName("SDC2CH-13");
		mapping.setFctryCd("1D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		mapping = new T_BCON_MAPPING();	
		mapping.setBconId("9ffb1c749a203aaadbb461e9fb202017");
		mapping.setBconName("SDC2CH-14");
		mapping.setFctryCd("1D1");
		mapping.setId(inc.incrementAndGet());
		mapping.setSetupLc(SetupLcType.FRONT);
		bcons.add(mapping);
		
		bconMapRepo.saveAll(bcons);
		
	}

	@Override
	public int order() {
		return 0;
	}

}
