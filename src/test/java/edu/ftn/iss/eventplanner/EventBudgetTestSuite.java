package edu.ftn.iss.eventplanner;


import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("edu.ftn.iss.eventplanner")
@IncludeTags({"budget", "unit", "integration"})
public class EventBudgetTestSuite { }
