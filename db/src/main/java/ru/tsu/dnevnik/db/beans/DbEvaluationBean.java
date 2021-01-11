package ru.tsu.dnevnik.db.beans;

import ru.tsu.dnevnik.db.interfaces.DbEvaluation;

import javax.ejb.ApplicationException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

/**
 * Created by Alexey on 12.11.2016.
 */
@Singleton(mappedName = "DbEvaluationBean", name = "DbEvaluationBean")
@Startup
@TransactionAttribute(REQUIRES_NEW)
@ApplicationException(rollback = true)
public class DbEvaluationBean implements DbEvaluation{

}
