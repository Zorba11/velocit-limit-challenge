***GET api/v1/loadfunds***

    will fetch the inputs from inputs/input.txt file and process the Queue
    
    Response: "Queue processed !"
    
***Get api/v1/loadfunds***

    Should run call this endoint after running the `api/v1/loadfunds1`.
    This endpoint will compare the generated outputs/output.txt with the output file
    given in the requirement. The given output is stored as outputs/output.txt.
    
    If the files match
        response: "Output is same as the given output"
    If not 
        response: "Outputs don't match" 
        
 More on implementation - https://gelatinous-bayberry-9b3.notion.site/Vault-challenge-7c90645af1db488ea1c64e939ef214ce