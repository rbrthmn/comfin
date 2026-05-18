Generate a new spec YAML file for a feature that does not yet exist in `specs/`.

Steps:

1. Ask the user for:
   - Feature name in PascalCase (e.g. "OperationsScreen", "BalanceCard")
   - Module path (e.g. ":feature:home", ":feature:operations")
   - Package (e.g. "br.com.rbrthmn.home.ui.components.balancecard")
   - One-sentence description of what the feature does

2. Compute derived values:
   - `module_dir`: strip the leading colon, replace remaining colons with `/`
     e.g. `:feature:home` → `feature/home`
   - `file_name`: convert PascalCase to snake_case
     e.g. `BalanceCard` → `balance_card`, `OperationsScreen` → `operations_screen`
   - `output_path`: `specs/{module_dir}/{file_name}.yaml`
   - `id_prefix`: first letter of each PascalCase word, uppercase
     e.g. `BalanceCard` → `BC`, `OperationsScreen` → `OS`, `CreditCardBillsCard` → `CCB`

3. Read `specs/_template.yaml` for structure reference.

4. Write `output_path` with all template fields filled in. Replace every `XX` behavior ID prefix with the computed `id_prefix`. Use the user-supplied name, module, package, and description.

5. Do NOT generate any Kotlin files. Spec only. Tell the user the output path when done.