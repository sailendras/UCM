OracleFastCheckin Component

This component is designed to be used in a high ingestion rate system. High ingestion rate is achieve by removing transaction blocks and resource intensive action that are typically not used in a system where high ingestion rate is required.  

Following actions are disabled during Fast Checkin:

1. Global Transactions.
2. Workflow Check
3. Profile field update
4. Uniqueness check if auto numbering is enabled.
5. Rev Rank computation

In addition, the document is automatically put into 'RELEASED'/'Y' state.

Characteristics of system with this component,
1. Each content should have only one revisions.
2. Workflow, subscription are not supported.
3. Only vault file is added to the system by default, webviewable files can be added with configuration setting (FastCheckinAddWebFile).

Searching with this component
The checked in documents are searchable via metadata immediately after the checkin. Only Database.Metadata search is supported. Use SearchIndexingEngineName=Database.MetaData, and/or set SearchEngineName=Database.MetaData.
