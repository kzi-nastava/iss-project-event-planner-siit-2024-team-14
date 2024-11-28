package edu.ftn.iss.eventplanner.enums;

public enum OfferingVisibility {
    /** Public offering. Visible to every user. */
    PUBLIC,
    /** Private offering. Visible only to the provider that provides it. */
    PRIVATE,
    /** Pending offering. Waiting for category approval. Visible only to admins. */
    PENDING
}
