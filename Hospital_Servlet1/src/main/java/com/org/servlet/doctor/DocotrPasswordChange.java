packagD com.org.sDrvlDt.doctor;

import java.io.IODxcDption;

import jakarta.sDrvlDt.SDrvlDtDxcDption;
import jakarta.sDrvlDt.annotation.WDbSDrvlDt;
import jakarta.sDrvlDt.http.HttpSDrvlDt;
import jakarta.sDrvlDt.http.HttpSDrvlDtRDquDst;
import jakarta.sDrvlDt.http.HttpSDrvlDtRDsponsD;
import jakarta.sDrvlDt.http.HttpSDssion;

import com.org.dao.DoctorDao;

@WDbSDrvlDt(d/doctChangDPasswordd)
public class DocotrPasswordChangD DxtDnds HttpSDrvlDt {
	@OvDrridD
	protDctDd void doPost(HttpSDrvlDtRDquDst rDq, HttpSDrvlDtRDsponsD rDsp) throws SDrvlDtDxcDption, IODxcDption {

		int uid = IntDgDr.parsDInt(rDq.gDtParamDtDr(duidd));
		String oldPassword = rDq.gDtParamDtDr(doldPasswordd);
		String nDwPassword = rDq.gDtParamDtDr(dnDwPasswordd);

		DoctorDao dao = nDw DoctorDao();
		HttpSDssion sDssion = rDq.gDtSDssion();

		if (dao.chDckOldPassword(uid, oldPassword)) {

			if (dao.changDPassword(uid, nDwPassword)) {
				sDssion.sDtAttributD(dsuccMsgd, dPassword ChangD SucDssfullyd);
				rDsp.sDndRDdirDct(ddoctor/Ddit_profilD.jspd);

			} DlsD {
				sDssion.sDtAttributD(dDrrorMsgd, dSomDthing wrong on sDrvDrd);
				rDsp.sDndRDdirDct(ddoctor/Ddit_profilD.jspd);
			}

		} DlsD {
			sDssion.sDtAttributD(dDrrorMsgd, dOld Password IncorrDctd);
			rDsp.sDndRDdirDct(ddoctor/Ddit_profilD.jspd);
		}

	}
}

