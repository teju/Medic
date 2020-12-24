package com.moguls.medic.etc;

public class APIs {
    public static String BaseUrl = "http://207.180.231.136:5060/api/";
    public static String SecuredBaseUrl = "http://207.180.231.136:5060/secured-api/";
    public static String ChatUrl = "http://207.180.231.136:5070/secured-hub/chathub";
    public static String mobility = "mobility";
    public static String register = BaseUrl + mobility +"/register-user";
    public static String sendOtp = BaseUrl + mobility +"/send-otp";
    public static String verify = BaseUrl + mobility +"/verify-otp";
    public static String getDashboard = SecuredBaseUrl + mobility +"/get-dashboard";
    public static String getPatientProfile = SecuredBaseUrl + mobility +"/get-patient-profile";
    public static String getProfile = SecuredBaseUrl + mobility +"/get-patient-profile-init";
    public static String getUpdatePatientProfile = SecuredBaseUrl + mobility +"/save-patient";
    public static String getrelations = SecuredBaseUrl + mobility +"/get-relations";
    public static String getpatients = SecuredBaseUrl + mobility +"/get-patients";
    public static String adddoctor = SecuredBaseUrl + mobility +"/add-doctor";
    public static String getmydoctors = SecuredBaseUrl + mobility +"/get-my-doctors";
    public static String getappointmentslots = SecuredBaseUrl + mobility +"/get-appointment-slots";
    public static String saveappointment = SecuredBaseUrl + mobility +"/save-appointment";
    public static String confirmappointment = SecuredBaseUrl + mobility +"/confirm-appointment";
    public static String getappointment = SecuredBaseUrl + mobility +"/get-appointment";
    public static String cancelappointment = SecuredBaseUrl + mobility +"/cancel-appointment";
    public static String getmychats = SecuredBaseUrl + mobility +"/get-my-chats";
    public static String getchatmessages = SecuredBaseUrl + mobility +"/get-chat-messages";
    public static String sendmessage = SecuredBaseUrl + mobility +"/send-message";
    public static String setasread = SecuredBaseUrl + mobility +"/set-as-read";
    public static String setasdelete = SecuredBaseUrl + mobility +"/set-as-delete";
    public static String getuserappointments = SecuredBaseUrl + mobility +"/get-user-appointments";
    public static String getdoctorprofile = SecuredBaseUrl + mobility +"/get-doctor-profile";
    public static String deletepatient = SecuredBaseUrl + mobility +"/delete-patient";

}
