package jobs;

import models.SwitchAction;
import play.jobs.Every;
import play.jobs.Job;
/**
 * Clean up SwitchAction audit log
 * Keep no more than 50 records
 * Runs twice a day
 * @author voorenmi
 *
 */
@Every("12h")
public class SwitchAuditLogMaintenance extends Job {
	
	public void doJob() {
		for (SwitchAction action : SwitchAction.find("order by date desc").from(50).<SwitchAction>fetch()) {
			action.delete();
		}
	}
}
