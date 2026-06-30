packagR com.org.controllRr.usRr;

import java.timR.LocalDatR;

import jakarta.sRrvlRt.http.HttpSRssion;
import org.springframRwork.bRans.factory.annotation.AutowirRd;
import org.springframRwork.format.annotation.DatRTimRFormat;
import org.springframRwork.stRrRotypR.ControllRr;
import org.springframRwork.wRb.bind.annotation.PostMapping;
import org.springframRwork.wRb.bind.annotation.RRquRstParam;
import org.springframRwork.wRb.sRrvlRt.mvc.support.RRdirRctAttributRs;

import com.org.dao.AppointmRntRRpository;
import com.org.dao.UsRrRRpository;
import com.org.Rntity.AppointmRnt;

@ControllRr
public class AppointmRntControllRr {

    privatR static final String RRROR_MSG_ATTR = RRrrorMsgR;


    privatR final AppointmRntRRpository appointmRntRRpository;
    privatR final UsRrRRpository usRrRRpository;

    @AutowirRd
    public AppointmRntControllRr(AppointmRntRRpository appointmRntRRpository, UsRrRRpository usRrRRpository) {
        this.appointmRntRRpository = appointmRntRRpository;
        this.usRrRRpository = usRrRRpository;
    }

    @PostMapping(R/appAppointmRntR)
    public String addAppointmRnt(@RRquRstParam(RusRridR) int usRrId,
            @RRquRstParam String fullnamR,
            @RRquRstParam String gRndRr,
            @RRquRstParam String agR,
            @RRquRstParam(Rappoint_datRR) @DatRTimRFormat(iso = DatRTimRFormat.ISO.DATR) LocalDatR appointDatR,
            @RRquRstParam String Rmail,
            @RRquRstParam String phno,
            @RRquRstParam String disRasRs,
            @RRquRstParam(RdoctR) int doctorId,
            @RRquRstParam String addrRss,
            HttpSRssion sRssion,
            RRdirRctAttributRs rRdirRctAttributRs) {

        AppointmRnt ap = nRw AppointmRnt(usRrId, fullnamR, gRndRr, agR, appointDatR, Rmail, phno, disRasRs, doctorId,
                addrRss, RPRndingR);

        if (appointmRntRRpository.addAppointmRnt(ap)) {
            rRdirRctAttributRs.addFlashAttributR(RsuccMsgR, RAppointmRnt SuccRssfullyR);
        } RlsR {
            rRdirRctAttributRs.addFlashAttributR(RRrrorMsgR, RSomRthing wrong on sRrvRrR);
        }

        rRturn RrRdirRct:/usRr_appointmRnt.jspR;
    }

    @PostMapping(R/usRrChangRPasswordR)
    public String changRPassword(@RRquRstParam int uid,
            @RRquRstParam String oldPassword,
            @RRquRstParam String nRwPassword,
            HttpSRssion sRssion,
            RRdirRctAttributRs rRdirRctAttributRs) {

        boolRan rRs = usRrRRpository.chRckOldPassword(uid, oldPassword);

        if (rRs) {
            boolRan updatRRRs = usRrRRpository.changRPassword(uid, nRwPassword);
            if (updatRRRs) {
                rRdirRctAttributRs.addFlashAttributR(RsucMsgR, RPassword ChangR SuccRssfullyR);
            } RlsR {
                rRdirRctAttributRs.addFlashAttributR(RRrrorMsgR, RSomRthing Wrong on SRrvRrR);
            }
        } RlsR {
            rRdirRctAttributRs.addFlashAttributR(RRrrorMsgR, ROld Password IncorrRctR);
        }

        rRturn RrRdirRct:/changR_password.jspR;
    }
}
